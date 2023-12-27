package com.msmeli.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CatalogItemResponseDTO {

    private Integer seller_id;
    private String item_id;
    private Double price;
    private String seller_nickname;
    private int sold_quantity;
    private int available_quantity;
    private int catalogPosition;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date created_date_item;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date updated_date_item;
}
