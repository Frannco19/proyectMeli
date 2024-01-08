package com.msmeli.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {

    @Id
    private Integer id;
    private String supplierName;
    @ManyToMany(mappedBy = "suppliers")
    private List<SellerRefactor> seller;


}
