package com.msmeli.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SellerRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seller_rating_id;

    private Double negative;
    private Double neutral;
    private Double positive;

}
