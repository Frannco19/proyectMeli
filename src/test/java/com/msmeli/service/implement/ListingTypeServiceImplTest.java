package com.msmeli.service.implement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.msmeli.model.ListingType;
import com.msmeli.repository.ListingTypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ListingTypeServiceImplTest {

    @Mock
    private ListingTypeRepository listingTypeRepository;

    @InjectMocks
    private ListingTypeServiceImpl listingTypeService;

    @Test
    public void testGetListingTypeName() {
        String typeId = "someTypeId";
        String expectedName = "SomeTypeName";
        ListingType listingType = new ListingType();
        listingType.setId(typeId);
        listingType.setName(expectedName);

        Mockito.when(listingTypeRepository.getListingTypeName(ArgumentMatchers.eq(typeId))).thenReturn(listingType);

        String result = listingTypeService.getListingTypeName(typeId);

        Assertions.assertEquals(expectedName, result);
    }

    @Test
    public void testGetListingTypeNameNotFound() {
        String typeId = "nonExistentTypeId";

        Mockito.when(listingTypeRepository.getListingTypeName(ArgumentMatchers.eq(typeId))).thenReturn(null);

        String result = listingTypeService.getListingTypeName(typeId);

        Assertions.assertNull(result);
    }

    @Test
    public void testGetListingTypeNameNullListingType() {
        String typeId = "someTypeId";

        Mockito.when(listingTypeRepository.getListingTypeName(ArgumentMatchers.eq(typeId))).thenReturn(null);

        String result = listingTypeService.getListingTypeName(typeId);

        Assertions.assertNull(result);
    }
}
