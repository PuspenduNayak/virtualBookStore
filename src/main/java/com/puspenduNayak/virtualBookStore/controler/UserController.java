package com.puspenduNayak.virtualBookStore.controler;

import com.puspenduNayak.virtualBookStore.entity.User;
import com.puspenduNayak.virtualBookStore.service.BookService;
import com.puspenduNayak.virtualBookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<?> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean flag = userService.isPresent(userName);
        if (flag) {
            User user = userService.findByUserName(userName);
            return new ResponseEntity<>(user, HttpStatus.FOUND);
        }
        return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);

    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User old = userService.findByUserName(userName);
        old.setUserName(user.getUserName() != null && !user.getUserName().equals("") ?user.getUserName():old.getUserName());
        old.setPassword(user.getPassword() != null && !user.getPassword().equals("") ?user.getPassword():old.getPassword());
        old.setEmail(user.getEmail() != null && !user.getEmail().equals("") ?user.getEmail():old.getEmail());
        boolean flag = userService.saveUser(old);
        if (flag)
            return new ResponseEntity<>("SUCCESSFUL", HttpStatus.NO_CONTENT);

        return new ResponseEntity<>("ERROR", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean flag = userService.deleteUser(userName);
        if (flag)
            return new ResponseEntity<>("SUCCESSFUL", HttpStatus.NO_CONTENT);

        return new ResponseEntity<>("FAILED", HttpStatus.BAD_REQUEST);
    }
}
