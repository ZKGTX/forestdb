package com.zerokikr.forestdb.exception;

public class MeasureNotFoundException extends RuntimeException {

    public MeasureNotFoundException(Long id)
    {
        super ("No measure with id " + id);
    }
}
