package com.puspenduNayak.virtualBookStore.repository;

import com.puspenduNayak.virtualBookStore.entity.Book;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, ObjectId> {
    List<Book> findByGenre(String genre);
    List<Book> findByAuthor(String genre);
}
