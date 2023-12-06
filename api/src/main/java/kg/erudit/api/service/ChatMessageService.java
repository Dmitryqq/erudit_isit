package kg.erudit.api.service;

import kg.erudit.common.inner.chat.ChatMessage;
import kg.erudit.common.inner.chat.MessageStatus;
import kg.erudit.db.repository.ChatMessageRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
//    @Autowired
    private final MongoOperations mongoOperations;

    public ChatMessageService(ChatMessageRepository chatMessageRepository, ChatRoomService chatRoomService, MongoOperations mongoOperations) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
        this.mongoOperations = mongoOperations;
    }

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(Integer senderId, Integer recipientId) {
        return chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(senderId, recipientId, MessageStatus.RECEIVED);
    }

    public List<ChatMessage> findChatMessages(Integer senderId, Integer recipientId, Integer limit) {
        Optional<String> chatId = chatRoomService.getChatId(senderId, recipientId, false);

        List<ChatMessage> messages = chatId.map(cId -> chatMessageRepository.findByChatId(cId, limit)).orElse(new ArrayList<>());

        if (!messages.isEmpty())
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);

        return messages;
    }

    public ChatMessage findById(String id) throws Exception {
        return chatMessageRepository.findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return chatMessageRepository.save(chatMessage);
                })
                .orElseThrow(() -> new Exception("can't find message (" + id + ")"));
    }

    public void updateStatuses(Integer senderId, Integer recipientId, MessageStatus status) {
        Query query = new Query(
                Criteria
                        .where("senderId").is(senderId)
                        .and("recipientId").is(recipientId));
        Update update = Update.update("status", status);
        mongoOperations.updateMulti(query, update, ChatMessage.class);
    }
}
