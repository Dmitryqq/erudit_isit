package kg.erudit.db.repository;

import kg.erudit.common.inner.chat.ChatMessage;
import kg.erudit.common.inner.chat.MessageStatus;
import kg.erudit.common.inner.notifications.Notification;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationsRepository extends MongoRepository<Notification, String> {
    @Aggregation(pipeline = {
            "{ '$match': { 'userId' : ?0 } }",
            "{ '$sort' : { 'timestamp' : 1 } }",
            "{ '$limit' : ?1 }"
    })
    List<Notification> findByUserId(Integer userId, Integer limit);
}
