package com.msmeli.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "seller", catalog = "msmeli")
public class Seller{

    @Id
    @Basic(optional = false)
    @Column(name = "seller_id")
    private Integer sellerId;
    @Column(name = "nickname")
    private String nickname;
    @JoinColumn(name = "seller_reputation_id", referencedColumnName = "seller_reputation_id")
    @ManyToOne
    private SellerReputation sellerReputationId;
    @OneToMany(mappedBy = "sellerId")
    private List<Item> itemList;



}