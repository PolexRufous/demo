package com.thinkjava.demo.entities;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Hero {
  private String id;
  private String name;
  private String description;
  private String power;
}
