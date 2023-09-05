package com.msmeli.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    private String item_id;
    private Integer seller_id;
    private Double price;
    private Integer available_quantity;
    private Integer sold_quantity;
    private String category_id;

}
