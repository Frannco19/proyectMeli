package com.msmeli.dto.response;

import com.msmeli.dto.FeeDetailsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeeResponseDTO {
    private Double sale_fee_amount;
    private FeeDetailsDTO details;

}
