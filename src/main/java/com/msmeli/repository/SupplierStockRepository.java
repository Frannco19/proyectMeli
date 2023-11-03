package com.msmeli.repository;

import com.msmeli.model.SupplierStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierStockRepository extends JpaRepository<SupplierStock, Long> {
}
