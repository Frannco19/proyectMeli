package com.msmeli.dto.response;

import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImageAndSkuDTO {

    private String sku;
    private String image_url;
    private String status_condition;
    private Date created_date_item;
    private Date updated_date_item;

}
