package kg.erudit.api.service;

import kg.erudit.common.inner.chat.ChatRoom;
import kg.erudit.db.repository.ChatRoomRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository) {
        this.chatRoomRepository = chatRoomRepository;
    }

    public Optional<String> getChatId(Integer senderId, Integer recipientId, boolean createIfNotExist) {
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (!createIfNotExist) {
                        return Optional.empty();
                    }
                    String chatId = String.format("%s_%s", senderId, recipientId);

                    ChatRoom senderRecipient = ChatRoom.builder()
                            .chatId(chatId)
                            .senderId(senderId)
                            .recipientId(recipientId)
                            .build();

                    ChatRoom recipientSender = ChatRoom.builder()
                            .chatId(chatId)
                            .senderId(recipientId)
                            .recipientId(senderId)
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }

    public Map<Integer, String> getChatUsers(Integer userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findBySenderIdOrRecipientId(userId);
        log.info(chatRooms);
        Map<Integer, String> chatUserList = new LinkedHashMap<>();
        if (chatRooms.isEmpty()) {
            return chatUserList;
        }
        for (ChatRoom chatRoom: chatRooms) {
            if (!chatRoom.getRecipientId().equals(userId))
                chatUserList.put(chatRoom.getRecipientId(), chatRoom.getChatId());
            else
                chatUserList.put(chatRoom.getSenderId(), chatRoom.getChatId());
        }
        return chatUserList;
    }
}
