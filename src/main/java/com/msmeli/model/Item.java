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

    @OneToOne
    @JoinColumn(name="product_id")
    private Product product;

    private String title;

    private String condition;

    @OneToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    private Double price;

    private Double original_price;

    private Integer sold_quantity;

    private Integer available_quantity;

    @ManyToOne
    private Seller seller;



}
