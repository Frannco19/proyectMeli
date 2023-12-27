package com.msmeli.dto.feign;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.msmeli.dto.AttributesDTO;
import com.msmeli.dto.PicturesDTO;
import jakarta.annotation.Nullable;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemFeignDTO {
    private String id;
    private String title;
    private String catalog_product_id;
    private String catalog_listing;
    private int price;
    private int sold_quantity;
    private int available_quantity;
    private String listing_type_id;
    private String category_id;
    private Date date_created;
    private String thumbnail;
    private int seller_id;
    private String status;
    private String condition;
    private List<AttributesDTO> attributes;
    private List<PicturesDTO> pictures;

}
