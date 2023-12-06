package kg.erudit.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import kg.erudit.api.util.JwtUtil;
import kg.erudit.common.inner.chat.ChatMessage;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.*;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {
    private final ObjectMapper mapper = new JsonMapper();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = accessor.getCommand();

//        switch (command) {
//            case CONNECT -> {
//                if (!JwtUtil.checkJwtToken(accessor))
//                    throw new JwtException("");
////
//                Claims claims = JwtUtil.validateToken(accessor);
//                if (claims == null || claims.get("role") == null) {
//                    throw new JwtException("");
//                }
//                break;
//            }
//        }

        if (command != StompCommand.CONNECT && command != StompCommand.SEND)
            return message;

        if (!JwtUtil.checkJwtToken(accessor))
            throw new JwtException("");
//
        Claims claims = JwtUtil.validateToken(accessor);
        if (claims == null || claims.get("role") == null) {
            throw new JwtException("");
        }

        if (!command.equals(StompCommand.SEND))
            return message;

        try {
            ChatMessage chatMessage = mapper.readValue((byte[]) message.getPayload(), ChatMessage.class);
            chatMessage.setSenderId((Integer) claims.get("id"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        ChannelInterceptor.super.postSend(message, channel, sent);
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        ChannelInterceptor.super.afterReceiveCompletion(message, channel, ex);
    }
}
