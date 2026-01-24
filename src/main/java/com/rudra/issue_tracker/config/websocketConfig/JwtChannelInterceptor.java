package com.rudra.issue_tracker.config.websocketConfig;

import com.rudra.issue_tracker.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null) {
                authHeader = accessor.getFirstNativeHeader("authorization");
            }

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (jwtTokenProvider.validateToken(token)) {
                    String userId = jwtTokenProvider.getUserIdFromToken(token);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userId, null, List.of());

                    accessor.setUser(authentication);
                    accessor.getSessionAttributes().put("SPRING_SECURITY_CONTEXT", authentication);
                    System.out.println("WebSocket Connected - Authenticated User ID: " + userId);
                } else {
                    System.err.println("WebSocket Connection Denied - Invalid Token");
                }
            }
        } else if (accessor != null) {
            // Restore authentication for non-CONNECT messages from session attributes
            if (accessor.getUser() == null && accessor.getSessionAttributes() != null) {
                UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) accessor
                        .getSessionAttributes()
                        .get("SPRING_SECURITY_CONTEXT");

                if (authentication != null) {
                    accessor.setUser(authentication);
                }
            }
        }

        return message;
    }
}