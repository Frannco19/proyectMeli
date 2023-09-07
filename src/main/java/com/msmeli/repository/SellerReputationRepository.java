package com.msmeli.repository;

import com.msmeli.model.SellerReputation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerReputationRepository extends JpaRepository<SellerReputation, Integer> {
}
