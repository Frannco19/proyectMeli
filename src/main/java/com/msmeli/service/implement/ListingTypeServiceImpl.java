package com.msmeli.service.implement;

import com.msmeli.model.ListingType;
import com.msmeli.repository.ListingTypeRepository;
import com.msmeli.service.services.ListingTypeService;
import org.springframework.stereotype.Service;

@Service
public class ListingTypeServiceImpl implements ListingTypeService {

    private final ListingTypeRepository listingTypeRepository;

    public ListingTypeServiceImpl(ListingTypeRepository listingTypeRepository) {
        this.listingTypeRepository = listingTypeRepository;
    }

    @Override
    public String getListingTypeName(String id) {
        ListingType listingType = listingTypeRepository.getListingTypeName(id);
        return listingType.getName();
    }
}
