package kg.erudit.common.inner.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ChatRoom {
    @Id
    private String id;
    private String chatId;
    private Integer senderId;
    private Integer recipientId;

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id='" + id + '\'' +
                ", chatId='" + chatId + '\'' +
                ", senderId=" + senderId +
                ", recipientId=" + recipientId +
                '}';
    }
}
