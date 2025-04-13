package com.puspenduNayak.virtualBookStore.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "books")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

        @Id
        private ObjectId id;
        @NonNull
        private String title;
        private String author;
        private String genre;
        private double price;
        private LocalDateTime date;
        private String pdfUrl;
}
