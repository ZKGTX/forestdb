package com.zerokikr.forestdb.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super ("No user with id " + id);
    }

    public UserNotFoundException(String email) {
        super ("No user with emai " + email);
    }

}
