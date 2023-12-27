package com.msmeli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.msmeli.dto.response.CatalogItemResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
