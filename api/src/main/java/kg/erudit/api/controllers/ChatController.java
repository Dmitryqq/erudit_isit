package kg.erudit.api.controllers;

import kg.erudit.api.service.ChatMessageService;
import kg.erudit.api.service.ChatRoomService;
import kg.erudit.api.service.ServiceWrapper;
import kg.erudit.common.inner.chat.ChatMessage;
import kg.erudit.common.inner.chat.ChatNotification;
import kg.erudit.common.resp.GetListResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/messages")
@Log4j2
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final ServiceWrapper serviceWrapper;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService, ChatRoomService chatRoomService, ServiceWrapper serviceWrapper) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
        this.serviceWrapper = serviceWrapper;
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setId(UUID.randomUUID().toString().replace("-", ""));
        var chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.get());
        chatMessage.setTimestamp(new Date());

        ChatMessage saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(chatMessage.getRecipientId().toString(), "/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
//                        saved.getSenderName(),
                        saved.getContent(),
                        saved.getTimestamp()));
    }

    @GetMapping("/list")
    public ResponseEntity<GetListResponse> getMessages() {
        return new ResponseEntity<>(serviceWrapper.getMessages(), HttpStatus.OK);
    }

//    @GetMapping("/{senderId}/{recipientId}/count")
//    public ResponseEntity<Long> countNewMessages(@PathVariable Integer senderId, @PathVariable Integer recipientId) {
//        return ResponseEntity.ok(chatMessageService.countNewMessages(senderId, recipientId));
//    }

    @GetMapping("/history/{recipientId}")
    public ResponseEntity<GetListResponse> findChatMessages(@PathVariable Integer recipientId,
                                                            @RequestParam(required = false, defaultValue = "20") Integer limit) {
        return ResponseEntity.ok(serviceWrapper.getChatMessages(recipientId, limit));
    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<?> findMessage(@PathVariable String id) throws Exception {
//        return ResponseEntity.ok(chatMessageService.findById(id));
//    }
}
