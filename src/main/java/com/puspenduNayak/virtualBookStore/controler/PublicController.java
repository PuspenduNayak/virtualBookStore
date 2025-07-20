package com.puspenduNayak.virtualBookStore.controler;

import com.puspenduNayak.virtualBookStore.entity.User;
import com.puspenduNayak.virtualBookStore.service.UserDetailsServiceImpl;
import com.puspenduNayak.virtualBookStore.service.UserService;
import com.puspenduNayak.virtualBookStore.utils.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@CrossOrigin("*")
@Slf4j
@Tag(name = "Public APIs", description = "Signup, Login, Logout Users & Health Check")
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

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            jwtUtil.blacklistToken(token);
            return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("No token found", HttpStatus.BAD_REQUEST);
    }
}
