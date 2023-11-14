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

    Page<Item> findBySkuContaining(String sku, Pageable pageable);

    Page<Item> findByIdContaining(String id, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE ((?2 = 'id' AND i.id like ?1) OR (?2 = 'sku' AND i.sku like ?1)) AND ((?3 = -1 AND i.catalog_position != ?3) OR (?3 = -2 AND i.catalog_position >= ?3)) AND ((?4 = 'null' AND i.status != ?4) OR i.status = ?4)")
    Page<Item> findByFilters(String searchInput, String searchType, int inCatalogue, String isActive, Pageable pageable);
    Page<Item> findAllBySellerId(Integer sellerId, Pageable pageable);
}
