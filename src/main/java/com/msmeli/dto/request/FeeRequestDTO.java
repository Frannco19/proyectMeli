package com.msmeli.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeeRequestDTO {

    private Double price;

    private String categoryId;

    private String ListingTypeId;

}
