package com.msmeli.dto.response;

import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CatalogItemResponseDTO {

    private Double price;
    private String seller_nickname;
    private int sold_quantity;
    private int available_quantity;
    private String category_id;

    private String listing_type_name;

    private Integer position;
    private Date created_date_item;
    private Date updated_date_item;


}
