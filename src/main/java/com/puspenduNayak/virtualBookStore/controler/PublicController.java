package com.puspenduNayak.virtualBookStore.controler;

import com.puspenduNayak.virtualBookStore.entity.User;
import com.puspenduNayak.virtualBookStore.service.EmailService;
import com.puspenduNayak.virtualBookStore.service.UserDetailsServiceImpl;
import com.puspenduNayak.virtualBookStore.service.UserService;
import com.puspenduNayak.virtualBookStore.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    @GetMapping("/health-check")
    public String healthCheck(){
        return "ok";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@RequestBody User user){

        boolean flag = userService.saveNewUser(user);
        if (flag) {
            return new ResponseEntity<>("SUCCESSFUL", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("UNSUCCESSFUL", HttpStatus.NOT_FOUND);
    }

    // Login Endpoint (Session-Based, Handled by Spring Security)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }
        catch (Exception e){
            log.error("Exception accured while createAuthenticationToken ", e);
            return new ResponseEntity<>("Incorrect username and password", HttpStatus.BAD_REQUEST);
        }
    }
}
