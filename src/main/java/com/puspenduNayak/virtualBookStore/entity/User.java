package com.puspenduNayak.virtualBookStore.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    private ObjectId id;
    @Indexed(unique = true)
    @NotNull
    private String userName;
    private String email;
    @NotNull
    private String password;

    @DBRef(lazy = true)
    private List<Book> booksEntries;
    private List<String> roles;
}
