package com.puspenduNayak.virtualBookStore.controler;

import com.puspenduNayak.virtualBookStore.entity.Book;
import com.puspenduNayak.virtualBookStore.entity.User;
import com.puspenduNayak.virtualBookStore.service.BookService;
import com.puspenduNayak.virtualBookStore.service.FileUploadService;
import com.puspenduNayak.virtualBookStore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
@CrossOrigin("*")
@Slf4j
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping("id/{myId}")
    public ResponseEntity<?> getBooksById(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean flag = userService.isPresent(userName);
        if (flag) {
            Optional<Book> book = bookService.findById(myId);
            if (book.isPresent()) {
                return new ResponseEntity<>(book, HttpStatus.OK);
            }
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("genre/{genre}")
    public ResponseEntity<?> getBooksByGenre(@PathVariable String genre) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean flag = userService.isPresent(userName);
        if (flag) {
            List<Book> all = bookService.getBooksByGenre(genre);
            if (all != null) {
                return new ResponseEntity<>(all, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("author/{author}")
    public ResponseEntity<?> getBooksByAuthor(@PathVariable String author) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean flag = userService.isPresent(userName);
        if (flag) {
            List<Book> all = bookService.getBooksByAuthor(author);
            if (all != null) {
                return new ResponseEntity<>(all, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all-books")
    public ResponseEntity<?> getAllBook() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean flag = userService.isPresent(userName);
        if (flag) {
            List<Book> all = bookService.getAllBooks();
            if (all != null) {
                return new ResponseEntity<>(all, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    /*@PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        try {
            if (bookService.saveBook(book, userName))
                return new ResponseEntity<>(book, HttpStatus.CREATED);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Exception occur when add book: ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }*/

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Book> uploadBook(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("genre") String genre,
            @RequestParam("price") double price
    ) {
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            String pdfUrl = fileUploadService.uploadFile(file);

            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(genre);
            book.setPrice(price);
            book.setDate(LocalDateTime.now());
            book.setPdfUrl(pdfUrl);

            if (bookService.saveBook(book, userName)) {
                return new ResponseEntity<>(book, HttpStatus.CREATED);
            }

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Error uploading book PDF", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("id/{myId}")
    public ResponseEntity<?> updateBookById(@PathVariable ObjectId myId, @RequestBody Book newBook) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        List<Book> collect = user.getBooksEntries().stream().filter(x -> x.getId().equals(myId)).toList();
        if (!collect.isEmpty() && user.getRoles() != null &&
                user.getRoles().stream().anyMatch(x->x.equals("ADMIN"))) {
            Optional<Book> book = bookService.findById(myId);
            Book old = book.get();
            old.setAuthor(newBook.getAuthor() != null && !newBook.getAuthor().equals("") ? newBook.getAuthor() : old.getAuthor());
            old.setTitle(newBook.getTitle() != null && !newBook.getTitle().equals("") ? newBook.getTitle() : old.getTitle());
            old.setPrice(Optional.ofNullable(newBook.getPrice()).orElse(old.getPrice()));
            old.setGenre(newBook.getGenre() != null && !newBook.getGenre().equals("") ? newBook.getGenre() : old.getGenre());
            bookService.saveBook(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteBook(@PathVariable ObjectId myId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        boolean flag = bookService.deleteById(myId, userName);
        if (flag)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
