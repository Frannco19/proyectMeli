package com.msmeli.dto.feign;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemFeignDTO {
    private String id;
    private String title;
    private String catalog_product_id;
    private Double price;
    private int sold_quantity;
    private int available_quantity;
    private String listing_type_id;
    private int catalog_position;
    private String seller_nickname;
    private String category_id;
}
