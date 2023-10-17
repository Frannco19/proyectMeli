package com.msmeli.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "sellertransaction", catalog = "msmeli")
public class SellerTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "seller_transaction_id")
    private Integer sellerTransactionId;
    @Column(name = "canceled")
    private Integer canceled;
    @Column(name = "completed")
    private Integer completed;
    @Column(name = "period")
    private String period;
    @Column(name = "total")
    private Integer total;
    @OneToMany(mappedBy = "sellerTransactionId")
    private List<SellerReputation> sellerreputationList;


}
