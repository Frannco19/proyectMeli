package com.msmeli.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employees")
public class Employee extends UserEntity{
    private String nombre;
    private String apellido;
    private String rol;
    @ManyToOne
    @JoinColumn(name = "sellerRefactor_id")
    private SellerRefactor sellerRefactor;
}
