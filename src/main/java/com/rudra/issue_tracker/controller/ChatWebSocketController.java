package com.rudra.issue_tracker.controller;

import com.rudra.issue_tracker.controller.dto.ChatMessageRequest;
import com.rudra.issue_tracker.controller.dto.ChatMessageResponse;
import com.rudra.issue_tracker.controller.dto.ReadReceiptEvent;
import com.rudra.issue_tracker.model.Message;
import com.rudra.issue_tracker.service.wbsocket.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(
            @Payload ChatMessageRequest request,
            Principal principal) {
        if (principal == null) {
            System.err.println("ERROR: Principal is null - user not authenticated");
            throw new IllegalStateException("User not authenticated");
        }

        // Get userId from principal (set by JwtChannelInterceptor)
        Long senderId;
        try {
            senderId = Long.parseLong(principal.getName());
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Cannot parse userId from principal: " + principal.getName());
            throw new IllegalStateException("Invalid user ID format");
        }

        Message message = chatService.sendMessage(
                senderId,
                request.getReceiverId(),
                request.getContent());

        ChatMessageResponse response = ChatMessageResponse.builder()
                .conversationId(message.getConversation().getId())
                .messageId(message.getId())
                .senderId(senderId)
                .content(message.getContent())
                .sequenceNo(message.getSequenceNo())
                .sentAt(message.getSentAt())
                .build();

        messagingTemplate.convertAndSend(
                "/topic/conversation." + message.getConversation().getId(),
                response);
    }

    @MessageMapping("/chat.read")
    public void markAsRead(
            @Payload ReadReceiptEvent event,
            Principal principal) {
        if (principal == null) {
            System.err.println("ERROR: Principal is null - user not authenticated");
            throw new IllegalStateException("User not authenticated");
        }

        Long userId;
        try {
            userId = Long.parseLong(principal.getName());
        } catch (NumberFormatException e) {
            System.err.println("ERROR: Cannot parse userId from principal: " + principal.getName());
            throw new IllegalStateException("Invalid user ID format");
        }

        chatService.markConversationAsRead(userId, event.getConversationId());

        messagingTemplate.convertAndSend(
                "/topic/conversation." + event.getConversationId() + ".receipts",
                userId);

        System.out.println(">>> Read receipt sent successfully");
    }
}