package com.msmeli.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "category", catalog = "msmeli")
public class Category{

    @Id
    @Basic(optional = false)
    @Column(name = "category_id")
    private String categoryId;
    @Column(name = "category_name")
    private String categoryName;
    @OneToMany(mappedBy = "category_id")
    private List<Item> itemList;



}
