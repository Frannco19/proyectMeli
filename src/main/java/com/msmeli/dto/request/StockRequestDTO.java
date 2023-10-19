package com.msmeli.dto.request;

import com.msmeli.dto.StockDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockRequestDTO {
    private Long user_id;
    private List<StockDTO> content;
}
