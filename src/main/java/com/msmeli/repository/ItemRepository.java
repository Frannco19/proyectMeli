package com.msmeli.repository;

import com.msmeli.dto.response.CreateItemDTO;
import com.msmeli.model.Item;
import com.msmeli.model.SellerRefactor;
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
//TODO CAMBIAR RANGO catalog_position CUANDO SE AGREDE EL MODULO DE CONSTO Y STOCK
    @Query("SELECT i FROM Item i WHERE ((:searchType = 'id' AND i.id like :searchInput) OR (:searchType = 'sku' AND i.sku like :searchInput)) AND ((:isCatalogue = TRUE AND i.catalog_listing = 'true' ) OR (:isCatalogue = FALSE AND i.catalog_listing = 'false')) AND ((:isActive = 'null' AND i.status != :isActive) OR i.status = :isActive) AND i.sellerRefactor = :seller")
    Page<Item> findByFilters(@Param("searchInput") String searchInput, @Param("searchType") String searchType, @Param("isCatalogue") boolean isCatalogue, @Param("isActive") String isActive, @Param("seller") SellerRefactor seller, Pageable pageable);
    Page<Item> findAllBySellerId(Integer sellerId, Pageable pageable);
    @Query("SELECT i FROM Item i WHERE i.sellerRefactor = :sellerId")
    List<Item> findAllBySellerRefactorId(Long sellerId);
}
