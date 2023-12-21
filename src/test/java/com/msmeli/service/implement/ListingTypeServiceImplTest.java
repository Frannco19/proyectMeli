package com.msmeli.service.implement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.msmeli.model.ListingType;
import com.msmeli.repository.ListingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

        when(listingTypeRepository.getListingTypeName(eq(typeId))).thenReturn(listingType);

        // Act
        String result = listingTypeService.getListingTypeName(typeId);

        // Assert
        assertEquals(expectedName, result);
    }

    @Test
    public void testGetListingTypeNameNotFound() {
        // Arrange
        String typeId = "nonExistentTypeId";

        // Simula que el repositorio devuelve null
        when(listingTypeRepository.getListingTypeName(eq(typeId))).thenReturn(null);

        // Act
        String result = listingTypeService.getListingTypeName(typeId);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetListingTypeNameNullListingType() {
        // Arrange
        String typeId = "someTypeId";

        // Simula que el repositorio devuelve null
        when(listingTypeRepository.getListingTypeName(eq(typeId))).thenReturn(null);

        // Act
        String result = listingTypeService.getListingTypeName(typeId);

        // Assert
        assertNull(result);
    }


}
