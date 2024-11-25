package com.gestion.microservice_notification_management.repository;

import com.gestion.microservice_notification_management.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}