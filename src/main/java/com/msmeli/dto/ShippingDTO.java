package com.msmeli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingDTO {

    private boolean free_shipping;

    private String logistic_type;

    private boolean store_pick_up;

}
