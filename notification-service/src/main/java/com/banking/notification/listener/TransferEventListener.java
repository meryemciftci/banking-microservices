package com.banking.notification.listener;

import com.banking.notification.entity.Notification;
import com.banking.notification.entity.NotificationType;
import com.banking.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransferEventListener {

    private final NotificationRepository notificationRepository;

    @RabbitListener(queues = "notification-queue")
    public void handleTransferEvent(TransferEvent event) {
        log.info("Received notification event for transfer: {}", event.getTransferId());

        // Notification for sender
        Notification senderNotification = Notification.builder()
                .userId(0L)
                .title("Transfer Sent")
                .message(String.format("You sent %.2f TL to %s. Description: %s",
                        event.getAmount(), event.getReceiverIban(),
                        event.getDescription() != null ? event.getDescription() : "-"))
                .type(NotificationType.TRANSFER_SENT)
                .build();
        notificationRepository.save(senderNotification);

        // Notification for receiver
        Notification receiverNotification = Notification.builder()
                .userId(0L)
                .title("Transfer Received")
                .message(String.format("You received %.2f TL from %s. Description: %s",
                        event.getAmount(), event.getSenderIban(),
                        event.getDescription() != null ? event.getDescription() : "-"))
                .type(NotificationType.TRANSFER_RECEIVED)
                .build();
        notificationRepository.save(receiverNotification);

        log.info("Notifications saved for transfer: {}", event.getTransferId());
    }
}