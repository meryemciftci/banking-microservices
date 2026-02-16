package com.banking.notification.service;

import com.banking.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // TODO: Send notification (triggered by RabbitMQ)
    // TODO: Get user notifications
    // TODO: Mark as read
}