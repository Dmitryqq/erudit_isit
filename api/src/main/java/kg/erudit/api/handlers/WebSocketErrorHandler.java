package kg.erudit.api.handlers;

import io.jsonwebtoken.JwtException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Component
public class WebSocketErrorHandler extends StompSubProtocolErrorHandler {
    @Override
    public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
        Throwable exception = ex;
        if (exception instanceof MessageDeliveryException) {
            exception = exception.getCause();
        }
//
//        if (exception instanceof UnauthorizedException)
//        {
//            return handleUnauthorizedException(clientMessage, exception);
//        }
        if (exception instanceof JwtException) {
            return handleAccessDeniedException(exception);
        }

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> handleAccessDeniedException(Throwable ex) {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
//        accessor.setMessage("403");
        accessor.setNativeHeader("code", "403");
        return MessageBuilder.createMessage(ex.getMessage().getBytes(), accessor.getMessageHeaders());
    }
}
