package com.msmeli.repository;

import com.msmeli.dto.response.CreateItemDTO;
import com.msmeli.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    @Query("SELECT new com.msmeli.dto.response.CreateItemDTO(i.catalog_product_id, i.title) FROM Item i")
    List<CreateItemDTO> getItemAtribbutes();

}
