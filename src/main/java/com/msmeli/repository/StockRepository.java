package com.msmeli.repository;

import com.msmeli.model.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
    @Query("SELECT s FROM Stock s WHERE s.sku = ?1 ORDER BY s.register_date LIMIT 1")
    Stock findBySku(String sku);

    @Query("SELECT sum(s.available_quantity) FROM Stock s WHERE s.sku = ?1")
    Integer getTotalBySku(String sku);

    @Query("SELECT s FROM Stock s WHERE s.user_id.seller.id = ?1")
    List<Stock> findAllBySellerId(Long sellerId);

    @Query("SELECT s FROM Stock s WHERE s.user_id.seller.id = ?1")
    Page<Stock> findAllBySellerId(Long id, Pageable pageable);
}
