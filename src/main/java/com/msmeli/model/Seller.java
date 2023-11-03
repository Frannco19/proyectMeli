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
@Table(name = "seller", catalog = "msmeli")
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sellerId;
    @Column(name = "nickname")
    private String nickname;
    @JoinColumn(name = "seller_reputation_id", referencedColumnName = "seller_reputation_id")
    @ManyToOne
    private SellerReputation sellerReputationId;
    @ManyToMany
    @JoinTable(name = "sellers_suppliers", joinColumns = @JoinColumn(name = "seller_id"), inverseJoinColumns = @JoinColumn(name = "supplier_id"))
    private List<Supplier> suppliers;
}
