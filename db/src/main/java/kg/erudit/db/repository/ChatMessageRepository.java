package kg.erudit.db.repository;

import kg.erudit.common.inner.chat.ChatMessage;
import kg.erudit.common.inner.chat.MessageStatus;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    Integer countBySenderIdAndRecipientIdAndStatus(Integer senderId, Integer recipientId, MessageStatus status);
    ChatMessage findTopByChatId(String chatId);
    @Aggregation(pipeline = {
            "{ '$match': { 'chatId' : ?0 } }",
            "{ '$sort' : { 'timestamp' : 1 } }",
            "{ '$limit' : ?1 }"
    })
    List<ChatMessage> findByChatId(String chatId, Integer limit);
}
