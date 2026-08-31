package EnterpriseProcurementSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import EnterpriseProcurementSystem.entity.Admin;
import EnterpriseProcurementSystem.entity.Notification;
import EnterpriseProcurementSystem.entity.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser(User user);

    List<Notification> findByUserAndIsReadFalse(User user);

    List<Notification> findByAdmin(Admin admin);

    List<Notification> findByAdminAndIsReadFalse(Admin admin);
}