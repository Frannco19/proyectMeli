package com.msmeli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.dto.response.SellerResponseDTO;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellerDTO {
    private SellerResponseDTO seller;
    private List<ItemResponseDTO> results;
    private PagingDTO paging;
}
