package com.puspenduNayak.virtualBookStore.service;

import com.puspenduNayak.virtualBookStore.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

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

    public void sendUserAccountDetails(User user, String tempPassword) {
        String role = String.join(", ", user.getRoles());
        String emailContent = "Id: " + user.getId() + "\n"
                + "Username: " + user.getUserName() + "\n"
                + "Password: " + tempPassword + "\n"
                + "Role: " + role;

        sendMail(user.getEmail(), "Account Created (Details)", emailContent);
    }
}
