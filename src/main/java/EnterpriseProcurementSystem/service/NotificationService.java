package EnterpriseProcurementSystem.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import EnterpriseProcurementSystem.entity.Admin;
import EnterpriseProcurementSystem.entity.Notification;
import EnterpriseProcurementSystem.entity.Request;
import EnterpriseProcurementSystem.entity.User;
import EnterpriseProcurementSystem.enums.NotificationType;
import EnterpriseProcurementSystem.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(
            String message,
            User user,
            Request request,
            NotificationType type) {

        Notification notification = new Notification();

        notification.setMessage(message);
        notification.setUser(user);
        notification.setRequest(request);
        notification.setType(type);
        notification.setRead(false);
        notification.setCreatedDate(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    public Notification createAdminNotification(
            String message,
            Admin admin,
            Request request,
            NotificationType type) {

        Notification notification = new Notification();

        notification.setMessage(message);
        notification.setAdmin(admin);
        notification.setRequest(request);
        notification.setType(type);
        notification.setRead(false);
        notification.setCreatedDate(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findByUser(user);
    }

    public List<Notification> getUnreadNotifications(User user) {
        return notificationRepository.findByUserAndIsReadFalse(user);
    }

    public List<Notification> getAdminNotifications(Admin admin) {
        return notificationRepository.findByAdmin(admin);
    }

    public List<Notification> getUnreadAdminNotifications(Admin admin) {
        return notificationRepository.findByAdminAndIsReadFalse(admin);
    }

    public Notification markAsRead(Long id) {

        Notification notification =
                notificationRepository.findById(id).orElse(null);

        if (notification != null) {
            notification.setRead(true);
            return notificationRepository.save(notification);
        }

        return null;
    }
}