package com.rudra.issue_tracker.service.wbsocket;

import com.rudra.issue_tracker.exceptions.NotFoundException;
import com.rudra.issue_tracker.model.*;

import com.rudra.issue_tracker.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

        private final UserRepository userRepository;
        private final ConversationRepository conversationRepository;
        private final MessageRepository messageRepository;
        private final MessageReceiptRepository messageReceiptRepository;

        private final ConversationStatusRepository conversationStatusRepository;
        private final DeliveryStatusRepository deliveryStatusRepository;
        private final MessageReceiptStatusRepository messageReceiptStatusRepository;
        private final ContentTypeRepository contentTypeRepository;

        public Message sendMessage(
                        Long senderId,
                        Long receiverId,
                        String content) {
                // 1️⃣ Identity validation
                if (senderId.equals(receiverId)) {
                        throw new IllegalArgumentException("User cannot message themselves");
                }

                User sender = userRepository.findById(senderId)
                                .orElseThrow(() -> new NotFoundException("Sender not found"));

                User receiver = userRepository.findById(receiverId)
                                .orElseThrow(() -> new NotFoundException("Receiver not found"));

                // 2️Normalize participants (CRITICAL)
                User participantA = sender.getId() < receiver.getId() ? sender : receiver;
                User participantB = sender.getId() < receiver.getId() ? receiver : sender;

                // 3️ Resolve conversation (get-or-create)
                Conversation conversation = conversationRepository
                                .findDirectConversation(participantA, participantB)
                                .orElseGet(() -> createNewConversation(participantA, participantB));

                // 4 Authorization check
                if (!isParticipant(conversation, sender)) {
                        throw new SecurityException("User is not part of this conversation");
                }

                if (!conversation.getStatus().getName().equals("ACTIVE")) {
                        throw new IllegalStateException("Conversation is not active");
                }

                // Create message
                Long nextSequence = messageRepository
                                .findMaxSequenceByConversation(conversation.getId())
                                .orElse(0L) + 1;

                Message message = Message.builder()
                                .conversation(conversation)
                                .sender(sender)
                                .sequenceNo(nextSequence)
                                .content(content)
                                .contentType(contentTypeRepository.findByName("TEXT").orElseThrow(
                                                () -> new NotFoundException("ContentType 'TEXT' not found")))
                                .sentAt(LocalDateTime.now())
                                .deliveryStatus(deliveryStatusRepository.findByName("SENT").orElseThrow(
                                                () -> new NotFoundException("DeliveryStatus 'SENT' not found")))
                                .build();

                messageRepository.save(message);

                // Create receipt for receiver
                MessageReceipt receipt = MessageReceipt.builder()
                                .message(message)
                                .user(receiver)
                                .status(messageReceiptStatusRepository.findByName("DELIVERED")
                                                .orElseThrow(() -> new NotFoundException(
                                                                "MessageReceiptStatus 'DELIVERED' not found")))
                                .timestamp(LocalDateTime.now())
                                .build();

                messageReceiptRepository.save(receipt);

                // Update conversation metadata
                conversation.setLastMessage(message);
                conversation.setLastActivityAt(LocalDateTime.now());
                conversationRepository.save(conversation);

                return message;
        }

        public void markConversationAsRead(Long userId, Long conversationId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new NotFoundException("User not found"));

                Conversation conversation = conversationRepository.findById(conversationId)
                                .orElseThrow(() -> new NotFoundException("Conversation not found"));

                MessageReceiptStatus readStatus = messageReceiptStatusRepository.findByName("READ")
                                .orElseThrow(() -> new NotFoundException("MessageReceiptStatus 'READ' not found"));

                // Find all DELIVERED receipts for this user in this conversation and mark them
                // as READ
                messageReceiptRepository.findByUserAndMessage_Conversation(user, conversation)
                                .stream()
                                .filter(receipt -> receipt.getStatus().getName().equals("DELIVERED"))
                                .forEach(receipt -> {
                                        receipt.setStatus(readStatus);
                                        receipt.setTimestamp(LocalDateTime.now());
                                        messageReceiptRepository.save(receipt);
                                });
        }

        // ---------- helpers ----------

        private Conversation createNewConversation(User a, User b) {
                ConversationStatus activeStatus = conversationStatusRepository.findByName("ACTIVE")
                                .orElseThrow(() -> new NotFoundException("ConversationStatus 'ACTIVE' not found"));

                return conversationRepository.save(
                                Conversation.builder()
                                                .type("DIRECT")
                                                .participantA(a)
                                                .participantB(b)
                                                .status(activeStatus)
                                                .lastActivityAt(LocalDateTime.now())
                                                .build());
        }

        private boolean isParticipant(Conversation conversation, User user) {
                return conversation.getParticipantA().getId().equals(user.getId())
                                || conversation.getParticipantB().getId().equals(user.getId());
        }
}
