package com.puspenduNayak.virtualBookStore.kafka; // Suggest putting consumers in a 'kafka' package

import com.puspenduNayak.virtualBookStore.dto.AccountDetailsEmailMessage; // Import your DTO
import com.puspenduNayak.virtualBookStore.service.EmailService; // Import your EmailService
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailMessageConsumer {

    @Autowired
    private EmailService emailService; // Inject your EmailService to send the mail

    private static final String ACCOUNT_CREATED_TOPIC = "user-account-creation"; // Make sure this matches!

    @KafkaListener(topics = ACCOUNT_CREATED_TOPIC, groupId = "account-email-sender-group")
    public void listenAccountCreation(AccountDetailsEmailMessage message) {
        log.info("Kafka consumer received account creation message for user: {}", message.to);

        // Now, use your EmailService to actually send the mail
        boolean mailSent = emailService.sendMail(message.to, message.subject, message.body);

        if (mailSent) {
            log.info("Successfully sent email to {} after consuming from Kafka.", message.to);
        } else {
            log.error("Failed to send email to {} after consuming from Kafka.", message.to);
            // In a production scenario, consider:
            // - Retrying the email send
            // - Moving the message to a Dead-Letter Queue (DLQ) for manual inspection
            // - Alerting an operations team
        }
    }
}