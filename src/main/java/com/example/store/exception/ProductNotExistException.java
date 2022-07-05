package com.example.store.exception;

public class ProductNotExistException extends IllegalArgumentException{
    public ProductNotExistException(String msg) {
        super(msg);
    }
}
