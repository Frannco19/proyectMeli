package com.msmeli.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.msmeli.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellerRequestDTO {
    private String nickname;
    private Long sellerId;
    private Supplier supplier;
}
