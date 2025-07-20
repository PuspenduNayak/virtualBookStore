package com.puspenduNayak.virtualBookStore.service;

import com.puspenduNayak.virtualBookStore.dto.AccountDetailsEmailMessage;
import com.puspenduNayak.virtualBookStore.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.springframework.kafka.KafkaException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired // <--- YOU NEED THIS FOR ObjectMapper
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    // <--- YOU NEED THESE FOR THE CONSTANTS
    private static final String ACCOUNT_CREATED_TOPIC = "user-account-creation";
    private static final long KAFKA_SEND_TIMEOUT_SECONDS = 3;

    public boolean sendMail(String to, String subject, String body){
        try{
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);
            javaMailSender.send(mail);
            return true;
        }
        catch (Exception e){
            log.error("Exception when sent mail: ", e);
            return false;
        }
    }

    /*public void sendUserAccountDetails(User user, String tempPassword) {
        String role = String.join(", ", user.getRoles());
        String emailContent = "Id: " + user.getId() + "\n"
                + "Username: " + user.getUserName() + "\n"
                + "Password: " + tempPassword + "\n"
                + "Role: " + role;

        sendMail(user.getEmail(), "Account Created (Details)", emailContent);
    }*/
    public void sendUserAccountDetails(User user, String tempPassword) {
        String role = String.join(", ", user.getRoles());
        String emailContent = "Id: " + user.getId() + "\n"
                + "Username: " + user.getUserName() + "\n"
                + "Password: " + tempPassword + "\n"
                + "Role: " + role;

        String emailSubject = "Account Created (Details)"; // Define subject for clarity

        // 1. Create a DTO for your Kafka message payload
        AccountDetailsEmailMessage messagePayload = new AccountDetailsEmailMessage(
                user.getEmail(),
                emailSubject,
                emailContent
        );

        boolean kafkaSendSuccess = false;
        String kafkaMessageString = null; // To hold the JSON string for logging

        try {
            // Convert DTO to JSON string for Kafka value if KafkaTemplate<String, String>
            // If KafkaTemplate<String, Object> and JsonSerializer is configured, this step is handled by Kafka
            // We're doing it explicitly here to ensure a fallbackable string version
            kafkaMessageString = objectMapper.writeValueAsString(messagePayload);

            // Attempt to send to Kafka with a timeout
            // Using ACCOUNT_CREATED_TOPIC for the topic name
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(ACCOUNT_CREATED_TOPIC, user.getId().toString(), messagePayload); // Use user ID as key

            // Wait for the message to be acknowledged by Kafka
            future.get(KAFKA_SEND_TIMEOUT_SECONDS, TimeUnit.SECONDS);

            kafkaSendSuccess = true;
            log.info("Successfully published account creation details to Kafka for user {}: {}", user.getUserName(), kafkaMessageString);

        } catch (TimeoutException e) {
            log.error("Kafka send timed out after {}s for user {}. Falling back to direct email. Error: {}",
                    KAFKA_SEND_TIMEOUT_SECONDS, user.getUserName(), e.getMessage());
        } catch (ExecutionException e) {
            log.error("Kafka producer execution error for user {}. Falling back to direct email. Cause: {}",
                    user.getUserName(), e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
        } catch (KafkaException e) {
            log.error("Kafka specific error (e.g., broker down) for user {}. Falling back to direct email. Error: {}",
                    user.getUserName(), e.getMessage());
        } catch (JsonProcessingException e) {
            log.error("Error serializing AccountDetailsEmailMessage for user {}. Falling back to direct email. Error: {}",
                    user.getUserName(), e.getMessage());
        } catch (Exception e) { // Catch any other unexpected exceptions
            log.error("Unexpected error during Kafka send for user {}. Falling back to direct email. Error: {}",
                    user.getUserName(), e.getMessage());
        }

        if (!kafkaSendSuccess) {
            // Fallback to direct email sending if Kafka send failed or timed out
            log.warn("Attempting to send direct email as Kafka send failed or timed out for user: {}", user.getUserName());
            boolean directMailSent = sendMail(user.getEmail(), emailSubject, emailContent);
            if (directMailSent) {
                log.info("Successfully sent direct email (fallback) for user: {}", user.getUserName());
            } else {
                log.error("Failed to send direct email (fallback) for user: {}", user.getUserName());
                // Consider further action here, like logging to a separate error file
                // or sending a critical alert if this email is absolutely vital.
            }
        }
    }
}
