package com.zerokikr.forestdb.enumeration;

public enum Unit {

    EMPTY(""),
    RUR("руб."),
    THOUSAND_RUR("тыс. руб.");

    private final String value;

    Unit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
