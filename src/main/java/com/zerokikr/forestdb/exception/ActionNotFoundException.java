package com.zerokikr.forestdb.exception;

public class ActionNotFoundException extends RuntimeException{

    public ActionNotFoundException(Long id)
    {
        super ("No action with id " + id);
    }
}
