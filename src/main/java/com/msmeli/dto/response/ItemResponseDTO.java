package com.msmeli.dto.response;

import com.msmeli.model.Category;
import com.msmeli.model.Seller;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDTO {

    private String item_id;
    private String title;
    private String catalog_product_id;
    private Double price;
    private int sold_quantity;
    private int available_quantity;
    private String listing_type_id;
    private int catalog_position;
    private String seller_nickname;
    private String category_id;
    private String statusCondition;
    private String urlImage;
    private String sku;
    private Date created_date_item;
    private Date updated_date_item;
}
