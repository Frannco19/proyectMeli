package com.msmeli.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OneProductResponseDTO {

    private String title;

    private String catalog_product_id;

    private Double price;

    private int sold_quantity;

    private int available_quantity;

    private String listing_type_name;

    private int catalog_position;

    private String seller_nickname;

    private String category_id;

    private Date created_date_item;

    private Date updated_date_item;

    private String status_condition;

    private String image_url;

    private String sku;

}
