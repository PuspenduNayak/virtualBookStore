package com.puspenduNayak.virtualBookStore.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "books")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

        @Id
        private ObjectId id;
        @NonNull
        private String title;
        private String author;
        private String genre;
        private double price;
        private LocalDateTime date;
}
