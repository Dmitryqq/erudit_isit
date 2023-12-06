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
@RequestMapping("/api/v1/notifications")
@Log4j2
public class NotificationController {
    private final ServiceWrapper serviceWrapper;

    public NotificationController(ServiceWrapper serviceWrapper) {
        this.serviceWrapper = serviceWrapper;
    }

    @GetMapping("/list")
    public ResponseEntity<GetListResponse> getMessages(@RequestParam(required = false, defaultValue = "20") Integer limit) {
        return new ResponseEntity<>(serviceWrapper.getNotifications(limit), HttpStatus.OK);
    }
}
