package kg.erudit.db.repository;

import kg.erudit.common.inner.chat.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(Integer senderId, Integer recipientId);
    List<ChatRoom> findBySenderIdOrRecipientId(Integer senderId, Integer recipientId);

    default List<ChatRoom> findBySenderIdOrRecipientId(Integer userId) {
        return findBySenderIdOrRecipientId(userId, userId);
    }
}
