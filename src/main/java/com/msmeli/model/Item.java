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
@Table(name = "item", catalog = "msmeli")
public class Item{
    @Id
    @Column(name = "item_id")
    private String itemId;
    @Column(name = "title")
    private String title;
    @Column(name = "status_condition")
    private String statusCondition;
    @Column(name = "price")
    private Double price;
    @Column(name = "sold_quantity")
    private Integer soldQuantity;
    @Column(name = "available_quantity")
    private Integer availableQuantity;

    @JoinColumn(name = "product_id", referencedColumnName = "productId")
    @ManyToOne
    private Product productId;
    @JoinColumn(name = "seller_id", referencedColumnName = "seller_id")
    @ManyToOne
    private Seller sellerId;
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @ManyToOne
    private Category categoryId;

}
