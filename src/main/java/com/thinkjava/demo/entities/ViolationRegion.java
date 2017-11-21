package com.thinkjava.demo.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ViolationRegion {
    SALTOVKA("Saltovka"),
    ALEKSEEVKA("Alekseevka"),
    BALKA("Balka");

    private String name;
}
