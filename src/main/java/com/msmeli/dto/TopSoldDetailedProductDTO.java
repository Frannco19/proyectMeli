package com.msmeli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.msmeli.dto.response.BuyBoxWinnerResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopSoldDetailedProductDTO {

    private String id;
    private String status;
    private int sold_quantity;
    private String name;
    private BuyBoxWinnerResponseDTO buy_box_winner;
}
