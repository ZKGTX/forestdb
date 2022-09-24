package com.zerokikr.forestdb.exception;

public class SubjectNotFoundException extends RuntimeException {

    public SubjectNotFoundException(Long id) {
        super ("No subject with id " + id);
    }
}
