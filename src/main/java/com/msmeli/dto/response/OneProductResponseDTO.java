package com.msmeli.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OneProductResponseDTO {

    private String item_id;
    private String title;
    private Double price;
    private int sold_quantity;
    private int available_quantity;
    private String listing_type_name;
    private int catalog_position;
    private String seller_nickname;
    private String category_id;
    private String sku;
    private Date created_date_item;
    private Date updated_date_item;

}
