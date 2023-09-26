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
@Table(name = "sellerreputation", catalog = "msmeli")
public class SellerReputation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "seller_reputation_id")
    private Integer sellerReputationId;
    @Column(name = "level_id")
    private String levelId;
    @Column(name = "power_seller_status")
    private String powerSellerStatus;
    @OneToMany(mappedBy = "sellerReputationId")
    private List<Seller> sellerList;
    @JoinColumn(name = "seller_transaction_id", referencedColumnName = "seller_transaction_id")
    @ManyToOne
    private SellerTransaction sellerTransactionId;
    @JoinColumn(name = "seller_rating_id", referencedColumnName = "seller_rating_id")
    @ManyToOne
    private SellerRating sellerRatingId;


}
