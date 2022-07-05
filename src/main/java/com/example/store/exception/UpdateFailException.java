package com.example.store.exception;

public class UpdateFailException extends IllegalArgumentException{
    public UpdateFailException(String msg) {
        super(msg);
    }
}
