package com.thinkjava.demo.entities;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Violation {
    private String carNumber;
    private String reason;
    private String address;
    private String region;
    private Car car;
}
