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
@Table(name = "sellerrating", catalog = "msmeli")
public class SellerRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "seller_rating_id")
    private Integer sellerRatingId;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "negative")
    private Double negative;
    @Column(name = "neutral")
    private Double neutral;
    @Column(name = "positive")
    private Double positive;
    @OneToMany(mappedBy = "sellerRatingId")
    private List<SellerReputation> sellerreputationList;


}
