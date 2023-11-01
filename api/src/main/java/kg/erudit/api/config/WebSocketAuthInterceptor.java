package kg.erudit.api.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = accessor.getCommand();

        if (command != StompCommand.CONNECT && command != StompCommand.SEND)
            return message;

//        if (!JwtUtil.checkJwtToken(accessor))
//            System.out.println("123");
//
//        Claims claims = JwtUtil.validateToken(accessor);
//        if (claims == null || claims.get("role") == null) {
////            return;
//        }

//        ChatMessage chatMessage = (ChatMessage) message.getPayload();
//        chatMessage.setSender(":LLLLL");
        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        System.out.printf("");
        ChannelInterceptor.super.postSend(message, channel, sent);
    }

    @Override
    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
        System.out.println();
        ChannelInterceptor.super.afterReceiveCompletion(message, channel, ex);
    }
}
