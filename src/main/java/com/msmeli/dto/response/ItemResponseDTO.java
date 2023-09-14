package com.msmeli.dto.response;

import com.msmeli.model.Category;
import com.msmeli.model.Seller;
import lombok.*;

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
    private Integer seller_id;
    private String category_id;
    private String statusCondition;
    private String urlImage;
    private String sku;
}
