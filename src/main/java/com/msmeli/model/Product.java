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
    private String product_id;

    private Integer sold_quantity;

    private String domain_id;

    private String name;

    private String family_name;

    private String type;

}
