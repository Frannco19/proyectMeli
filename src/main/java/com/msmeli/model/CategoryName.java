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
@Table(name = "categoryName", catalog = "msmeli")
public class CategoryName {


    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private String id;
    @Column(name = "Name")
    private String name;


}
