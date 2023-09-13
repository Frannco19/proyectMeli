package com.msmeli.dto.response;

import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateItemDTO {
    String catalog_product_id;
    String title;
}
