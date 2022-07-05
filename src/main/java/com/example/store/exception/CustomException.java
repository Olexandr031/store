package com.example.store.exception;

public class CustomException extends IllegalArgumentException{
    public CustomException(String msg) {
        super(msg);
    }
}
