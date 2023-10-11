package com.msmeli.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "listing_type", catalog = "msmeli")
public class ListingType {

    @Id
    private String id;

    private String name;


}
