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
        // Arrange
        String typeId = "someTypeId";
        String expectedName = "SomeTypeName";
        ListingType listingType = new ListingType();
        listingType.setId(typeId);
        listingType.setName(expectedName);

        Mockito.when(listingTypeRepository.getListingTypeName(ArgumentMatchers.eq(typeId))).thenReturn(listingType);

        // Act
        String result = listingTypeService.getListingTypeName(typeId);

        // Assert
        Assertions.assertEquals(expectedName, result);
    }

    @Test
    public void testGetListingTypeNameNotFound() {
        // Arrange
        String typeId = "nonExistentTypeId";

        // Simula que el repositorio devuelve null
        Mockito.when(listingTypeRepository.getListingTypeName(ArgumentMatchers.eq(typeId))).thenReturn(null);

        // Act
        String result = listingTypeService.getListingTypeName(typeId);

        // Assert
        Assertions.assertNull(result);
    }

    @Test
    public void testGetListingTypeNameNullListingType() {
        // Arrange
        String typeId = "someTypeId";

        // Simula que el repositorio devuelve null
        Mockito.when(listingTypeRepository.getListingTypeName(ArgumentMatchers.eq(typeId))).thenReturn(null);

        // Act
        String result = listingTypeService.getListingTypeName(typeId);

        // Assert
        Assertions.assertNull(result);
    }


}
