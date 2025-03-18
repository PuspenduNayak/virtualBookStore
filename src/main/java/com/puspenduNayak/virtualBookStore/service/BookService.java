package com.puspenduNayak.virtualBookStore.service;

import com.puspenduNayak.virtualBookStore.entity.Book;
import com.puspenduNayak.virtualBookStore.entity.User;
import com.puspenduNayak.virtualBookStore.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;


    public List<Book> getBooksByGenre(String genre) {
        return bookRepository.findByGenre(genre);
    }

    public List<Book> getBooksByAuthor(String author) {
        return bookRepository.findByAuthor(author);
    }

    @Transactional
    public boolean saveBook(Book book, String userName) {
        try {
            User user = userService.findByUserName(userName);
            if (user.getRoles().stream().anyMatch(x -> x.equals("ADMIN"))) {
                if (user.getBooksEntries() == null) {
                    user.setBooksEntries(new ArrayList<>());
                }
                book.setDate(LocalDateTime.now());
                Book saved = bookRepository.save(book);
                user.getBooksEntries().add(saved);
                userService.saveAdmin(user);
                return true;
            }

        } catch (Exception e) {
            throw new RuntimeException("An error occurred while saving the entry.", e);
        }
        return false;
    }

    public void saveBook(Book book) {
        try {
            bookRepository.save(book);
        } catch (Exception e) {
            log.error("Exception:", e);
        }
    }

    public Optional<Book> findById(ObjectId id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            if (user.getRoles().stream().anyMatch(x -> x.equals("ADMIN"))) {
                if (user.getBooksEntries() != null) {
                    removed = user.getBooksEntries().removeIf(x -> x.getId().equals(id));
                }
                if (removed) {
                    userService.saveAdmin(user);
                    bookRepository.deleteById(id);
                }
            }
        } catch (Exception e) {
            log.error("Error:", e);
        }
        return removed;
    }


    @Transactional
    public boolean deleteAll(User user) {
        try {
            for (Book book : user.getBooksEntries()) {
                bookRepository.deleteById(book.getId());
            }
            return true;
        } catch (Exception e){
            return false;
        }

    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

}

