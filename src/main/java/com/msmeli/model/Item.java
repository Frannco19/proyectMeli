package com.msmeli.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Item {

    @Id
    private String item_id;

    private String product;

    private String title;

    private String status_condition;

    private String category;

    private Double price;

    private Integer sold_quantity;

    private Integer available_quantity;

    private Integer seller;



}
