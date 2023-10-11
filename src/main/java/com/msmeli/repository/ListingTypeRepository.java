package com.msmeli.repository;

import com.msmeli.model.ListingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingTypeRepository extends JpaRepository<ListingType, String> {
}
