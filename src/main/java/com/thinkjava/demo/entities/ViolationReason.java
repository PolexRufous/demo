package com.thinkjava.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ViolationReason {
    GO_RED("Go to red color"),
    INCORRECT_PARKING("Parking in incorrect place"),
    CRASH("Crash accident");

    private String name;
}
