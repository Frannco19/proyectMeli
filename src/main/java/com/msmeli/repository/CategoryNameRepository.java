package com.msmeli.repository;

import com.msmeli.dto.response.CreateItemDTO;
import com.msmeli.model.Category;
import com.msmeli.model.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryNameRepository extends JpaRepository<CategoryName, String> {
}
