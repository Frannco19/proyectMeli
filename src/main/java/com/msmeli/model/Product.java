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
    @Column(name = "domain_id")
    private String domainId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "family_name")
    private String familyName;
    @Column(name = "product_type")
    private String productType;
    @OneToMany(mappedBy = "productId")
    private List<Item> itemList;


}
