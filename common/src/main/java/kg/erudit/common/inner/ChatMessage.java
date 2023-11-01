package kg.erudit.common.inner;

import lombok.Data;

@Data
public class ChatMessage {
    private String content;
    private String sender;
    private MessageType type;

    public enum MessageType {
        CHAT, LEAVE, JOIN
    }
}
