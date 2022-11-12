package com.zerokikr.forestdb.exception;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException(String name) {
        super("No role with name " + name);
    }
}
