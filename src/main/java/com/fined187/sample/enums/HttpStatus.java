package com.fined187.sample.enums;

import lombok.Getter;

import java.util.EnumSet;

@Getter
public enum  HttpStatus {
    INVALIDATED_PARAMETER(10, "INVALIDATED_PARAMETER"),
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found");

    private final int value;
    private final String responsePhrase;

    HttpStatus(int value, String responsePhrase) {
        this.value = value;
        this.responsePhrase = responsePhrase;
    }

    public static HttpStatus ofLegacyCode(int httpStatusCode) {
        return EnumSet.allOf(HttpStatus.class).stream()
                .filter(v -> v.getValue() == httpStatusCode)
                .findAny()
                .orElse(null);
    }
}
