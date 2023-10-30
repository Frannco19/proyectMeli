package com.msmeli.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CostResponseDTO {
    private double comision_fee;
    private double comision_discount;
    private double shipping;
    private double replacement_cost;
    private double profit;
    private double IIBB;
    private double total_cost;
    private double margin;
}
