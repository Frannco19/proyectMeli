package com.msmeli.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CatalogItemResponseDTO {

    private Integer seller_id;
    private String item_id;
    private Double price;
    private String seller_nickname;
    private int sold_quantity;
    private int available_quantity;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date created_date_item;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date updated_date_item;


}
