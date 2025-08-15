package com.puspenduNayak.virtualBookStore.controler;

import com.puspenduNayak.virtualBookStore.entity.Book;
import com.puspenduNayak.virtualBookStore.entity.User;
import com.puspenduNayak.virtualBookStore.service.BookService;
import com.puspenduNayak.virtualBookStore.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs", description = "Read, Update & Delete Admin, Delete Users")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getUsers() {
        List<User> all = userService.getAll();
        if (all != null) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>("ERROR", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        boolean flag = userService.saveNewAdmin(user);

        if (flag)
            return new ResponseEntity<>("SUCCESSFUL", HttpStatus.CREATED);

        return new ResponseEntity<>("ERROR", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("delete-user-by-admin")
    public ResponseEntity<?> deleteUserByAdmin(@RequestBody User user) {
        User delUser = userService.findByUserName(user.getUserName());
        if (delUser != null && delUser.getRoles() != null && !delUser.getRoles().stream().anyMatch(role -> role.equals("ADMIN"))) {
            boolean flag = userService.deleteUser(delUser.getUserName());
            if (flag)
                return new ResponseEntity<>("SUCCESSFUL", HttpStatus.NO_CONTENT);

            return new ResponseEntity<>("FAILED", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        List<Book> booksEntries = user.getBooksEntries();  // Assign to a variable
        if (booksEntries == null || booksEntries.isEmpty()) {
            userService.deleteUser(userName);
            return new ResponseEntity<>("SUCCESSFUL", HttpStatus.NO_CONTENT);
        }
        if (bookService.deleteAll(user)) {
            boolean flag = userService.deleteUser(userName);
            if (flag)
                return new ResponseEntity<>("SUCCESSFUL", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>("FAILED", HttpStatus.BAD_REQUEST);
    }
}
