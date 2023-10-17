package com.msmeli.repository;

import com.msmeli.model.ListingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingTypeRepository extends JpaRepository<ListingType, String> {

    @Query("SELECT l FROM ListingType l WHERE l.id = ?1")
    ListingType getListingTypeName(String id);

}
