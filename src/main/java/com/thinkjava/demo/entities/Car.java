package com.thinkjava.demo.entities;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Car {
    private String number;
    private String ownerFirstName;
    private String ownerLastName;
    private String registrationAddress;
}
