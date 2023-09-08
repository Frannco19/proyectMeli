package com.msmeli.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SellerReputation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seller_reputation_id;

    private String level_id;

    private String power_seller_status;

    @OneToOne
    private SellerTransaction sellerTransaction;

    @OneToOne
    private SellerRating sellerRating;

}
