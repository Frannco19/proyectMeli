package com.msmeli.service.implement;

import com.msmeli.model.ListingType;
import com.msmeli.repository.ListingTypeRepository;
import com.msmeli.service.services.ListingTypeService;
import org.springframework.stereotype.Service;

/**
 * Implementación de servicio para manejar operaciones relacionadas con tipos de listados.
 */
@Service
public class ListingTypeServiceImpl implements ListingTypeService {

    private final ListingTypeRepository listingTypeRepository;

    /**
     * Constructor que inicializa la implementación con el repositorio de tipos de listados.
     *
     * @param listingTypeRepository Repositorio de tipos de listados.
     */
    public ListingTypeServiceImpl(ListingTypeRepository listingTypeRepository) {
        this.listingTypeRepository = listingTypeRepository;
    }

    /**
     * Obtiene el nombre del tipo de listado asociado al ID proporcionado.
     *
     * @param id ID del tipo de listado.
     * @return Nombre del tipo de listado. Devuelve {@code null} si no se encuentra el tipo de listado.
     */
    @Override
    public String getListingTypeName(String id) {
        ListingType listingType = listingTypeRepository.getListingTypeName(id);
        return listingType.getName();
    }
}
