package de.ait.training.model;

import lombok.*;
//
//@Getter
//@Setter
//@ToString
//@AllArgsConstructor
//@NoArgsConstructor //создаем пустой конструктор
@Data
@AllArgsConstructor
public class Car {
    private long id;
    private String color;
    private String model;
    private double price;
}
