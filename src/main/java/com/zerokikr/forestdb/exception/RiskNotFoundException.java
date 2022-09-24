package com.zerokikr.forestdb.exception;

public class RiskNotFoundException extends RuntimeException{

    public RiskNotFoundException(Long id) {
        super ("No risk with id " + id);
    }
}
