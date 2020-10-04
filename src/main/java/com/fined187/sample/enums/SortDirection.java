package com.fined187.sample.enums;

import lombok.Getter;

@Getter
public enum  SortDirection {
    DESC(-1),
    ASC(1);

    private int value;

    private SortDirection(int value) {
        this.value = value;
    }
}
