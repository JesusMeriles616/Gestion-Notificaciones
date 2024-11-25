package com.gestion.microservice_notification_management.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.gestion.microservice_notification_management.repository.NotificationRepository;
import com.gestion.microservice_notification_management.model.Notification;

import java.util.Date;

@Component
public class FlightStatusUpdateListener {

    @Autowired
    private NotificationRepository notificationRepository;

    @KafkaListener(topics = "flightStatusUpdate", groupId = "my-kafka-group")
    public void listen(FlightStatusUpdateEvent event) {
        try {
            System.out.println("Received event for flight ID: " + event.getFlightId());
            Notification notification = new Notification();
            notification.setFlightId(event.getFlightId());
            notification.setUserId(1);  // Assuming a user is fixed
            notification.setMessage("Flight status updated to: " + event.getNewStatus());
            notification.setSentAt(new Date());
            notificationRepository.save(notification);
            System.out.println("Notification saved for flight ID: " + event.getFlightId());
        } catch (Exception e) {
            System.err.println("Error processing the Kafka event: " + e.getMessage());
        }
    }
}

