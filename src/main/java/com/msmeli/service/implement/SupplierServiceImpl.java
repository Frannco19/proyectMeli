package com.msmeli.service.implement;

import com.msmeli.dto.request.StockBySupplierRequestDTO;
import com.msmeli.dto.request.SupplierResquestDTO;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.Supplier;
import com.msmeli.model.SupplierStock;
import com.msmeli.repository.SupplierRepository;
import com.msmeli.service.services.SupplierService;
import com.msmeli.service.services.SupplierStockService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierStockService supplierStockService;
    private final ModelMapper modelMapper;

    public SupplierServiceImpl(SupplierRepository supplierRepository, SupplierStockService supplierStockService, ModelMapper modelMapper) {
        this.supplierRepository = supplierRepository;
        this.supplierStockService = supplierStockService;
        this.modelMapper = modelMapper;
    }

    public Supplier findById(Integer id) throws ResourceNotFoundException {
        return supplierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No supplier"));
    }

    /**
     * Metodo se encarga en crear/declarar Supplier en relacion de seller logueado
     */
    @Override
    @Transactional
    public void createSupplier(SupplierResquestDTO supplierResquestDTO) throws AppException {
        try {
            if (hasSupplier(supplierResquestDTO.getId())){
                Supplier newSupplier  = modelMapper.map(supplierResquestDTO, Supplier.class);
                supplierRepository.save(newSupplier);
            }
        }catch (Exception ex){
            throw new AppException("Error en la creacion de Proveedor","CreateSupplier",000,500);
        }
    }

    /**
     * Este metodo se encarga de devolver un True si no existe un Proveedor con el id enviado por parametro
     * @param id para Identificar la entidad en la base de datos
     * @return devuelve true si no se encontro ninguna entidad
     * @throws AppException
     */
    private boolean hasSupplier(Integer id) throws AppException {
       if(!supplierRepository.existsById(id)){
           return true;
       }else throw new AppException("Ya existe un proveedor con ese cuit", "createSupplier->hasSupplier", 000, 500);
    }

    public List<SupplierStock> uploadSupplierStock(StockBySupplierRequestDTO stockBySupplierRequestDTO) throws ResourceNotFoundException {
        Supplier supplier = findById(stockBySupplierRequestDTO.getSupplierId());
        return supplierStockService.create(supplier, stockBySupplierRequestDTO.getContent());
    }
}
