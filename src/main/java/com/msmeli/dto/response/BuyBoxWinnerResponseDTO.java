package com.msmeli.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.msmeli.dto.ShippingDTO;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyBoxWinnerResponseDTO {

    private String item_id;
    private String seller_nickname;
    private Integer seller_id;
    private Double price;
    private Integer sold_quantity;
    private Integer available_quantity;
    private String listing_type_id;
    private ShippingDTO shipping;

}
