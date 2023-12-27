package com.msmeli.dto.feign;

import com.msmeli.dto.PagingDTO;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemIdsResponseDTO {
    private List<String> results;
    private PagingDTO paging;
}
