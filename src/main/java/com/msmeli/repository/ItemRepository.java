package com.msmeli.repository;

import com.msmeli.dto.response.CreateItemDTO;
import com.msmeli.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {
    @Query("SELECT new com.msmeli.dto.response.CreateItemDTO(i.catalog_product_id, i.title) FROM Item i WHERE i.sellerId = ?1")
    List<CreateItemDTO> getItemAtribbutes(Integer selleId);

    @Query("SELECT i FROM Item i WHERE i.sellerId = ?1")
    Page<Item> getItemsBySellerId(Integer sellerId, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.catalog_product_id = ?1 ")
    Item findByProductId(String productId);

    @Query("SELECT i FROM Item i WHERE i.catalog_position != -1 AND i.sellerId = :sellerId")
    Page<Item> getCatalogItems(@Param("sellerId") Integer sellerId, Pageable pageable);

    List<Item> findBySkuContaining(String sku);

    List<Item> findByMlaContaining(String id);

//    List<Item> findByPublicationNumberContaining(String publicationNumber);

}
