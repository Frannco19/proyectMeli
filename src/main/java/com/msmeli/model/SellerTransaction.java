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
public class SellerTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seller_transaction_id;

    private Integer canceled;

    private Integer completed;

    private String period;

    private Integer total;

}
