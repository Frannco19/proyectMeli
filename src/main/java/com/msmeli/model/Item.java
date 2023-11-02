package com.msmeli.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "item", catalog = "msmeli")
public class Item {
    @Id
    private String id;

    private String title;

    private String catalog_product_id;
    @Column(length = 5000)
    private String description;
    private Double price;

    private int sold_quantity;

    private int available_quantity;

    private String listing_type_id;

    private int catalog_position;

    private int best_seller_position;

    private Integer sellerId;

    private String category_id;

    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime update_date_db;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_date_item;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_date_item;

    private String status_condition;

    private String image_url;

    private String sku;

    @OneToOne
    private Cost cost;
}
