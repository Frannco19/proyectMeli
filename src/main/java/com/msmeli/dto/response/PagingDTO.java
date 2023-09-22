package com.msmeli.dto.response;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PagingDTO {
    private Integer total;
    private Integer offset;
    private Integer limit;
}
