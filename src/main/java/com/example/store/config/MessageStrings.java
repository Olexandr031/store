package com.example.store.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageStrings {
    public static final String USER_NOT_PERMITTED = "User is not permitted to perform this operation";
    public static final String AUTH_TOKEN_NOT_PRESENT = "Authentication token not present";
    public static final String AUTH_TOKEN_NOT_VALID = "Authentication token not valid";
    public static final String USER_CREATED = "User created successfully";
    public static final String ID_NOT_PRESENT = "Primary key is required for updating";
    public static final String WRONG_PASSWORD = "Please check the password";
}
