//package kg.erudit.api.controllers;
//
//import kg.erudit.api.service.ServiceWrapper;
//import kg.erudit.common.inner.ChatMessage;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//@Controller
//@Log4j2
//public class ChatController {
//    private final ServiceWrapper serviceWrapper;
//    private final SimpMessagingTemplate messagingTemplate;
//
//
//    public ChatController(ServiceWrapper serviceWrapper, SimpMessagingTemplate messagingTemplate) {
//        this.serviceWrapper = serviceWrapper;
//        this.messagingTemplate = messagingTemplate;
//    }
//
//    @MessageMapping("/chat")
//    public void processMessage(@Payload ChatMessage chatMessage) {
//        var chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
//        chatMessage.setChatId(chatId.get());
//
//        ChatMessage saved = chatMessageService.save(chatMessage);
//        messagingTemplate.convertAndSendToUser(
//                chatMessage.getRecipientId(),"/queue/messages",
//                new ChatNotification(
//                        saved.getId(),
//                        saved.getSenderId(),
//                        saved.getSenderName()));
//    }
//
////    @MessageMapping("/chat.register")
////    @SendTo("/topic/public")
////    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
////        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
////        return chatMessage;
////    }
////
////    @MessageMapping("/chat.send")
////    @SendTo("/topic/public")
////    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
////        System.out.println(chatMessage);
////        return chatMessage;
////    }
//}
