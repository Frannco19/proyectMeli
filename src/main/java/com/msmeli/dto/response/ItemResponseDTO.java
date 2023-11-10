package com.msmeli.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.msmeli.util.TrafficLight;
import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemResponseDTO {

//    private String item_id;
    private String id;
    private String title;
    private String catalog_product_id;
    private Double price;
    private int sold_quantity;
    private int available_quantity;
    private String listing_type_id;
    private int catalog_position;
    private String seller_nickname;
    private String category_id;
    private String description;

    private int best_seller_position;

    private String status;
    @JsonProperty("condition")
    private String status_condition;
    private String image_url;
    private String sku;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date created_date_item;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date updated_date_item;
    private CostResponseDTO item_cost;
    private Integer total_stock;
    private TrafficLight trafficLight;
    private double winnerPrice;
}
