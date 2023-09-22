package com.msmeli.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemAttributesDTO {
    private List<AttributesDTO> attributes;
    private List<PicturesDTO> pictures;
    private String condition;
    private Date date_created;
    private Date last_updated;
}
