package com.msmeli.repository;

import com.msmeli.dto.response.CreateItemDTO;
import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.model.Item;
import com.msmeli.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    @Query("SELECT new com.msmeli.dto.response.CreateItemDTO(i.catalog_product_id, i.title) FROM Item i WHERE i.sellerId = ?1")
    List<CreateItemDTO> getItemAtribbutes(Integer selleId);

    @Query("SELECT i FROM Item i WHERE i.sellerId = ?1")
    List<Item> getItemsBySellerId(Integer sellerId);

    @Query("SELECT i FROM Item i WHERE i.catalog_product_id = ?1 ORDER BY i.catalog_position ASC")
    List<Item> getCatalogItems(String productId);

    @Query("SELECT i FROM Item i WHERE i.catalog_product_id = ?1")
    Item findByProductId(String productId);

}
