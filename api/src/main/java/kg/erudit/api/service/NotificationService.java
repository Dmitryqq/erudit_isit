package kg.erudit.api.service;

import kg.erudit.common.inner.notifications.Notification;
import kg.erudit.db.repository.NotificationsRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void deleteNotification(String notificationId) {
        notificationsRepository.deleteById(notificationId);
    }
}
