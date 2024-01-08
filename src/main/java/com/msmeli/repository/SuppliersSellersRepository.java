package com.msmeli.repository;

import com.msmeli.model.SuppliersSellers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuppliersSellersRepository extends JpaRepository<SuppliersSellers, Long> {

    @Query("SELECT ss FROM SuppliersSellers ss WHERE ss.supplier.id =?1 AND ss.seller.id =?2 AND ss.supplierStock.sku = ?3")
    Optional<SuppliersSellers> filterSupplierSeller(Integer supplierId, Long sellerId, String sku);

    List<SuppliersSellers> findAllBySellerId(Long id);

    @Query("SELECT ss FROM SuppliersSellers ss WHERE ss.seller.id =?1")
    Page<SuppliersSellers> getSuppliersSellersBySellerId(Long id, Pageable pageable);

    @Query("SELECT ss FROM SuppliersSellers ss WHERE ss.seller.id = ?2 AND ss.supplierStock.sku = ?1")
    Optional<SuppliersSellers> findBySkuAndSellerId(String sku, Long sellerId);
}
