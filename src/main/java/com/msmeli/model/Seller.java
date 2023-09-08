package com.msmeli.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Seller {

    @Id
    private Integer seller_id;

    private String nickname;

    @OneToOne
    private SellerReputation sellerReputation;

}
