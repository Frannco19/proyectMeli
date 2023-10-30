package com.msmeli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.msmeli.dto.response.BuyBoxWinnerResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BoxWinnerDTO {
    private BuyBoxWinnerResponseDTO buy_box_winner;

}
