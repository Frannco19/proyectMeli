package com.msmeli.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellerResponseDTO {
    private Integer id;
    private String nickname;
}
