package com.recruvia.backend.repository;

import com.recruvia.backend.entity.Notification;
import com.recruvia.backend.entity.User;
import com.recruvia.backend.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByUser(User user);

    List<Notification> findByUserAndIsRead(User user, boolean isRead);

    List<Notification> findByNotificationType(NotificationType notificationType);

}