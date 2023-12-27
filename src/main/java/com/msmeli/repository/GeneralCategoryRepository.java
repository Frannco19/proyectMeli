package com.msmeli.repository;

import com.msmeli.model.GeneralCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralCategoryRepository extends JpaRepository< GeneralCategory,String> {
}
