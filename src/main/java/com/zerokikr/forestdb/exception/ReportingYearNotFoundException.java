package com.zerokikr.forestdb.exception;

public class ReportingYearNotFoundException extends RuntimeException {

    public ReportingYearNotFoundException(Long id)
    {
        super ("No reporting year with id " + id);
    }
}
