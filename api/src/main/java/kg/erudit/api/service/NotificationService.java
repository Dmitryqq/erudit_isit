package kg.erudit.api.service;

import kg.erudit.common.inner.chat.ChatMessage;
import kg.erudit.common.inner.chat.ChatNotification;
import kg.erudit.common.inner.chat.MessageStatus;
import kg.erudit.common.inner.notifications.Notification;
import kg.erudit.common.inner.notifications.NotificationStatus;
import kg.erudit.db.repository.NotificationsRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationsRepository notificationsRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(NotificationsRepository notificationsRepository, SimpMessagingTemplate messagingTemplate) {
        this.notificationsRepository = notificationsRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public void saveAndSend(Notification notification) {
        notificationsRepository.save(notification);
        messagingTemplate.convertAndSendToUser(notification.getUserId().toString(), "/queue/notifications", notification);
    }

    public List<Notification> findNotifications(Integer userId, Integer limit) {
        return notificationsRepository.findByUserId(userId, limit);
    }
}
