package com.modsProject.modsProject.server.security.interceptors;

import com.modsProject.modsProject.server.dto.AuthDto;
import com.modsProject.modsProject.server.security.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class AuthInterceptor implements ChannelInterceptor {
    @Autowired
    private AuthService authService;

    @Autowired
    @Lazy
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        if (!StompCommand.SEND.equals(accessor.getCommand()) && !StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }

        String jwtToken = accessor.getFirstNativeHeader("jwt_token");
        String sessionKey = accessor.getFirstNativeHeader("session_key");

        AuthDto auth = authService.authenticate(jwtToken, sessionKey);

        if (auth == null) {
            if (sessionKey == null || sessionKey.isEmpty()) {//Если сессия не была указана
                //Продолжить как аноним
                accessor.setUser(() -> "-1");

                return message;
            }
            else { //Если сессия была, но авторизация не прошла - предложение авторизироваться (будет опция "продолжить как аноним/не входить")
                throw new MessageDeliveryException(message, "INVALID_SESSION");
            }
        }

        accessor.setUser(() -> auth.getCreatorId().toString());

        //Перехватываю только SEND-команды
        if (StompCommand.SEND.equals(accessor.getCommand())) {
            if (auth.hasNewJwt()) {
                messagingTemplate.convertAndSendToUser(
                        auth.getCreatorId().toString(),
                        "/queue/refresh",
                        auth.getNewJwtToken()
                );
            }
        }
        return message;
    }
}