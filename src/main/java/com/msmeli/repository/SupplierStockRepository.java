package com.msmeli.repository;

import com.msmeli.model.SupplierStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierStockRepository extends JpaRepository<SupplierStock, Long> {
    Optional<SupplierStock> findBySku(String sku);
}
