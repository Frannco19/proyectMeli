package com.msmeli.repository;

import com.msmeli.model.AllGeneralCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllGeneralCategoryRepository extends JpaRepository<AllGeneralCategory, String> {
}
