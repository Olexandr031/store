package com.example.store.exception;

public class CartItemNotExistException extends IllegalArgumentException{
    public CartItemNotExistException(String msg) {
        super(msg);
    }
}
