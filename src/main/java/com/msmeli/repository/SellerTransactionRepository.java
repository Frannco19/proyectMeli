package com.msmeli.repository;

import com.msmeli.model.SellerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerTransactionRepository extends JpaRepository<SellerTransaction, Integer> {
}
