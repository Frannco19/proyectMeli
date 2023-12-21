package com.msmeli.service.implement;

import static org.junit.jupiter.api.Assertions.*;

import com.msmeli.model.ListingType;
import com.msmeli.repository.ListingTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

    @ExtendWith(MockitoExtension.class)
    public class ListingTypeServiceImplTest {

        @Mock
        private ListingTypeRepository listingTypeRepository;

        @InjectMocks
        private ListingTypeServiceImpl listingTypeService;

        @Test
        public void testGetListingTypeName() {
            // Configurar comportamiento simulado para el mock
            String typeId = "1";  // Ingresa el typeId que desees
            ListingType listingType = new ListingType();
            listingType.setId(typeId);
            listingType.setName("ListingTypeName");

            when(listingTypeRepository.getListingTypeName(typeId)).thenReturn(listingType);

            // Llamar al método que estás probando
            String result = listingTypeService.getListingTypeName(typeId);

            // Verificar que los métodos necesarios se hayan llamado
            verify(listingTypeRepository, atLeastOnce()).getListingTypeName(typeId);

            // Realizar aserciones
            assertEquals("ListingTypeName", result);
        }


//    @Mock
//    private ListingTypeRepository listingTypeRepository;
//
//    @InjectMocks
//    private ListingTypeServiceImpl listingTypeService;
//
//    @Test
//    public void testGetListingTypeName() {
//        String id = "testId";
//        ListingType listingType = new ListingType();
//        listingType.setName("testName");
//
//        Mockito.when(listingTypeRepository.getListingTypeName(id)).thenReturn(listingType);
//
//        String result = listingTypeService.getListingTypeName(id);
//
//        assertEquals(listingType.getName(), result);
//    }
}