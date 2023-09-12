package com.msmeli.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "product", catalog = "msmeli")
public class Product{
    @Id()
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
    @Column(name = "product_id")
    private String productId;
    @Column(name = "sold_quantity")
    private Integer soldQuantity;

    @JoinColumn(name = "item_id", referencedColumnName = "item_id")
    @OneToMany()
    private List<Item> item_id;

    private Integer available_quantity;
    @Column(name = "product_name")
    private String productName;

    private String listing_type_id;
    private Double price;
    private String seller_name;


}
