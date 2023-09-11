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
    @Id
    @Basic(optional = false)
    @Column(name = "product_id")
    private String productId;
    @Column(name = "sold_quantity")
    private Integer soldQuantity;

    private Integer available_quantity;
    @Column(name = "product_name")
    private String productName;

    private String statusCondition;

    private String listing_type_id;
    private Double price;
    private String seller_name;


}
