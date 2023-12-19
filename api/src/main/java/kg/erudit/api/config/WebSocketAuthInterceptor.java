package kg.erudit.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import kg.erudit.api.util.JwtUtil;
import kg.erudit.common.inner.chat.ChatMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
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

        if (StompCommand.DISCONNECT.equals(command) || StompCommand.SUBSCRIBE.equals(command))
            return message;

        if (!JwtUtil.checkJwtToken(accessor)) {
            throw new JwtException("Токен не найден");
        }
//
        Claims claims = JwtUtil.validateToken(accessor);
        if (claims == null || claims.get("role") == null) {
            throw new JwtException("Валидация токена не пройдена");
        }

        try {
            if (StompCommand.SEND.equals(command)) {
                ChatMessage chatMessage = mapper.readValue((byte[]) message.getPayload(), ChatMessage.class);
                chatMessage.setSenderId((Integer) claims.get("id"));
                message = MessageBuilder.createMessage(mapper.writeValueAsBytes(chatMessage), accessor.getMessageHeaders());
            }
            setUpSpringWebsocketAuthentication(claims, accessor);
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

    private void setUpSpringWebsocketAuthentication(Claims claims, StompHeaderAccessor accessor) {
        String role = claims.get("role").toString();
        List<String> roles = new ArrayList<>();
        roles.add(role);
        Boolean pwdChangeRequired = (Boolean) claims.get("pwdChangeRequired");
        Integer userId = (Integer) claims.get("id");

        log.info("Authorized user '{}' with role '{}', pwdChangeRequired {}", claims.getSubject(), role, pwdChangeRequired);
        CustomAuthToken auth = new CustomAuthToken(claims.getSubject(), null,
                roles.stream().map(SimpleGrantedAuthority::new).toList(), pwdChangeRequired, userId);

        accessor.setUser(auth);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
