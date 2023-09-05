package com.msmeli.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private Integer id;
    private String item_id;
    private String seller_id;
    private Double price;
    private Integer available_quantity;
    private Integer sold_quantity;
    private String category_id;
}
