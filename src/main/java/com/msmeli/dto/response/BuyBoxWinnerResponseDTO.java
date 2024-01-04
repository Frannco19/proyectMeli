package com.msmeli.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.msmeli.dto.feign.ShippingDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuyBoxWinnerResponseDTO {

    private String item_id;
    private String seller_nickname;
    private Integer seller_id;
    private Double price;
    private Integer sold_quantity;
    private Integer available_quantity;
    private String listing_type_id;
    private Double original_price;
    private ShippingDTO shipping;

}
