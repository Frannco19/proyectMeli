package com.msmeli.dto.response;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PositionResponseDTO {

    private String id;
    private Integer position;
    private String type;

}
