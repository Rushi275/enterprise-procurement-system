package EnterpriseProcurementSystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import EnterpriseProcurementSystem.entity.Notification;
import EnterpriseProcurementSystem.entity.User;
import EnterpriseProcurementSystem.repository.UserRepository;
import EnterpriseProcurementSystem.service.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/{userId}")
    public List<Notification> getUserNotifications(@PathVariable Long userId) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return List.of();
        }

        return notificationService.getUserNotifications(user);
    }

    @GetMapping("/user/{userId}/unread")
    public List<Notification> getUnreadNotifications(@PathVariable Long userId) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return List.of();
        }

        return notificationService.getUnreadNotifications(user);
    }

    @PutMapping("/{id}/read")
    public Notification markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id);
    }
}