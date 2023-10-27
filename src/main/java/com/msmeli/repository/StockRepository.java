package com.msmeli.repository;

import com.msmeli.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {
//    @Query("SELECT s FROM Stock s WHERE s.sku = ?1 AND s.register_date = (SELECT MIN(s2.register_date) FROM Stock s2 WHERE s2.sku = ?1) ORDER BY s.register_date LIMIT 1")
    @Query("SELECT s FROM Stock s WHERE s.sku = ?1 ORDER BY s.register_date LIMIT 1")
    Stock findBySku(String sku);

    @Query("SELECT sum(s.available_quantity) FROM Stock s WHERE s.sku = ?1")
    Integer getTotalBySku(String sku);
}
