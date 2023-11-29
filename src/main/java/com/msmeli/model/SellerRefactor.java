package com.msmeli.model;

import com.msmeli.configuration.security.entity.UserEntityUserDetails;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sellers")
public class SellerRefactor extends UserEntity {
    private String meliID;
    private String refreshToken;
    private String tokenMl;
    @OneToMany(mappedBy = "sellerRefactor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees;
}
