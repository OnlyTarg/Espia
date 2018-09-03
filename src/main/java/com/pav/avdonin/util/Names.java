package com.pav.avdonin.util;

public enum Names {
    ESPIA_SERVER("EspiaServer"), KPP("КПП1"), KTP("КТП"), ESPIA_JL("EspiaJL"),;
    private String value;

    Names(String value) {
        this.value = value;

    }

    public String getValue() {
        return value;
    }
}