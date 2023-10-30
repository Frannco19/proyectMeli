package com.msmeli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeeDetailsDTO {

    private Double financing_add_on_fee;

    private Double fixed_fee;

    private Double gross_amount;

    private Double meli_percentage_fee;

    private Double percentage_fee;

}
