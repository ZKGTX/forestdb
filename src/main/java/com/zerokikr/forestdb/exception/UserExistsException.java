package com.zerokikr.forestdb.exception;

public class UserExistsException extends RuntimeException{

    public UserExistsException(String message) {
        super(message);
    }
}
