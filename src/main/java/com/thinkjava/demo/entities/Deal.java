package com.thinkjava.demo.entities;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Deal {
    private String id;
    private ProductType productType;
    private Hero seller;
    private Hero buyer;
    private Double price;
    private String description;
}
