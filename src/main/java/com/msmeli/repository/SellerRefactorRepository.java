package com.msmeli.repository;


import com.msmeli.model.SellerRefactor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRefactorRepository extends JpaRepository<SellerRefactor, Long> {
}
