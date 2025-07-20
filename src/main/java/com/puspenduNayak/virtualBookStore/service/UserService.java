package com.puspenduNayak.virtualBookStore.service;

import com.puspenduNayak.virtualBookStore.entity.User;
import com.puspenduNayak.virtualBookStore.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;


    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean saveNewUser(User user) {
        try {
            String plainPassword = user.getPassword();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepository.save(user);
            emailService.sendUserAccountDetails(user, plainPassword);
            return true;
        } catch (Exception e) {
            log.error("Exception occur when saveNew user:", e);
            return false;
        }
    }

    public boolean saveNewAdmin(User user) {
        try {
            String plainPassword = user.getPassword();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER", "ADMIN"));
            userRepository.save(user);
            emailService.sendUserAccountDetails(user, plainPassword);
            return true;
        } catch (Exception e) {
            log.error("Exception occur when saveNew user:", e);
            return false;
        }
    }

    public void saveAdmin(User user) {
        userRepository.save(user);
    }
    public boolean saveUser(User user) {
        try {
        userRepository.save(user);
            return true;
        }
        catch (Exception e){
            log.error("Exception: ", e);
            return false;
        }
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Transactional
    public boolean deleteUser(String userName) {
        try {
            User user = findByUserName(userName);
            userRepository.delete(user);
            return true;
        } catch (Exception e) {
            log.error("Exception: ", e);
            return false;
        }
    }

    public boolean isPresent(String userName) {
        User user = userRepository.findByUserName(userName);
        return user != null && user.getUserName().equals(userName);
    }

}
