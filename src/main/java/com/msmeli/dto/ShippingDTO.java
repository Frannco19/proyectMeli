package com.msmeli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingDTO {

    private String mode;
    private boolean free_shipping;

    private String logistic_type;

    private boolean store_pick_up;

}
