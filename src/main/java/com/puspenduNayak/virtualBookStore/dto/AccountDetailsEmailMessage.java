package com.puspenduNayak.virtualBookStore.dto;

// DTO for Kafka message payload
public class AccountDetailsEmailMessage {
    public String to;
    public String subject;
    public String body;

    public AccountDetailsEmailMessage(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    // Default constructor for Jackson deserialization
    public AccountDetailsEmailMessage() {}

    public String getTo() { return to; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }

    // Add setters if fields are private (recommended practice)
    public void setTo(String to) { this.to = to; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setBody(String body) { this.body = body; }

    @Override
    public String toString() {
        return "AccountDetailsEmailMessage{" +
                "to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
