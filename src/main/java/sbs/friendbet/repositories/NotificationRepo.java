package sbs.friendbet.repositories;

import org.springframework.data.repository.CrudRepository;
import sbs.friendbet.notification.Notification;

import java.util.List;

public interface NotificationRepo extends CrudRepository<Notification, Integer> {
    List<Notification> findAllByRecipientId(int userId);
}
