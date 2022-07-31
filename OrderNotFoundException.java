package com.example.store.exception;

public class OrderNotFoundException extends IllegalArgumentException{
    public OrderNotFoundException(String msg) {
        super(msg);
    }
}
