package com.example.demo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;
    @Indexed(unique = true) //unique usernames
    private String username;
    private String password;
    private String authTokenHash;
    private Instant tokenExpiresAt;
}
