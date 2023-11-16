package com.msmeli.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.msmeli.dto.request.SellerRequestDTO;
import com.msmeli.dto.request.StockRequestDTO;
import com.msmeli.dto.request.UserRegisterRequestDTO;
import com.msmeli.exception.AlreadyExistsException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.RoleEntity;
import com.msmeli.model.Supplier;
import com.msmeli.repository.RoleRepository;
import com.msmeli.repository.StockRepository;
import com.msmeli.repository.SupplierRepository;
import com.msmeli.repository.UserEntityRepository;
import com.msmeli.service.services.GeneralCategoryService;
import com.msmeli.service.services.ItemService;
import com.msmeli.service.services.SellerService;
import com.msmeli.service.services.StockService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DataInitializer {

    private final SellerService sellerService;
    private final SupplierRepository supplierRepository;
    private final RoleRepository roleRepository;
    private final StockRepository stockRepository;
    private final UserEntityRepository userEntityRepository;
    private final StockService stockService;
    private final ItemService itemService;
    private final GeneralCategoryService categoryService;

    public DataInitializer(SellerService sellerService, SupplierRepository supplierRepository, RoleRepository roleRepository, StockRepository stockRepository, UserEntityRepository userEntityRepository, StockService stockService, ItemService itemService, GeneralCategoryService categoryService) {
        this.sellerService = sellerService;
        this.supplierRepository = supplierRepository;
        this.roleRepository = roleRepository;
        this.stockRepository = stockRepository;
        this.userEntityRepository = userEntityRepository;
        this.stockService = stockService;
        this.itemService = itemService;
        this.categoryService = categoryService;
    }

    public void defaultSeller() {
        if (sellerService.findAll().isEmpty()) {
            SellerRequestDTO seller = new SellerRequestDTO();
            seller.setSellerId(1152777827l);
            seller.setNickname("Moro Tech");
            sellerService.create(seller);
        }
    }

    public Supplier defaultSupplier() {
        Supplier supplier = null;
        if (supplierRepository.findAll().isEmpty()) {
            supplier = new Supplier();
            supplier.setSupplierName("Kalydon Tools");
            supplier = supplierRepository.save(supplier);
        }
        return supplier;
    }


    public void defaultUser() throws AlreadyExistsException, ResourceNotFoundException {
        if (userEntityRepository.findAll().isEmpty()) {
            sellerService.createUser(new UserRegisterRequestDTO("user1", "123456", "123456", "mt.soporte.usuario@gmail.com", 1));
        }
    }

    public void defaultRoles() {
        if (roleRepository.findAll().isEmpty()) {
            for (Role role : Role.values()) {
                roleRepository.save(RoleEntity.builder().name(role).build());
            }
        }
    }

    public void defaulStock() throws IOException {
        if (stockRepository.findAll().isEmpty()) {
            ClassPathResource resource = new ClassPathResource("stock_example.json");
            ObjectMapper map = new ObjectMapper();
            StockRequestDTO requestDTO = map.readValue(resource.getInputStream(), StockRequestDTO.class);
            stockService.saveUserStock(requestDTO);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    @Order(5)
    public void fillBd() throws AlreadyExistsException, ResourceNotFoundException, IOException {
        defaultSeller();
        defaultSupplier();
        defaultRoles();
        defaultUser();
        defaulStock();
        itemService.createProductsCosts();
//        categoryService.createAll();
    }
}
