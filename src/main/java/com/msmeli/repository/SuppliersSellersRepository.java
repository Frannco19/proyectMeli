package com.msmeli.repository;

import com.msmeli.model.SuppliersSellers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuppliersSellersRepository extends JpaRepository<SuppliersSellers,Long> {

    @Query("SELECT ss FROM SuppliersSellers ss WHERE ss.supplier.id =?1 AND ss.seller.id =?2 AND ss.supplierStock.sku = ?3")
    Optional<SuppliersSellers> filterSupplierSeller(Long supplierId, Long sellerId, String sku);

    List<SuppliersSellers> findAllBySellerId(Long id);
}
