package kg.erudit.common.inner.notifications;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
@Builder
public class Notification {
    @Id
    private String id;
    private Integer userId;
    private String text;
    private NotificationType type;
    private NotificationStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm dd.MM.yyyy", timezone = "Asia/Bishkek")
    private Date timestamp;

    public Notification(Integer userId, String text, NotificationType type) {
        this.userId = userId;
        this.text = text;
        this.type = type;
        this.status = NotificationStatus.RECEIVED;
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", text='" + text + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", timestamp=" + timestamp +
                '}';
    }
}
