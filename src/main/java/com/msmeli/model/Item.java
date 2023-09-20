package com.msmeli.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "item", catalog = "msmeli")
public class Item{
    @Id
    private String item_id;

    private String title;

    private String catalog_product_id;

    private Double price;

    private int sold_quantity;

    private int available_quantity;

    private String listing_type_id;

    private int catalog_position;
    private Integer sellerId;
    private String category_id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime update_date_db;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created_date_item;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_date_item;

    private String statusCondition;

    private String urlImage;

    private String sku;
}
