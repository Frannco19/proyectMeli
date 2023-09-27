package com.msmeli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.msmeli.dto.response.CatalogItemResponseDTO;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemCatalogDTO {
    private PagingDTO paging;
    private List<CatalogItemResponseDTO> results;

}
