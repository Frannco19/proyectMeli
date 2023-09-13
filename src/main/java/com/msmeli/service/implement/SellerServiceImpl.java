package com.msmeli.service.implement;

import com.msmeli.model.Seller;
import com.msmeli.repository.SellerRepository;
import com.msmeli.service.services.SellerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellerServiceImpl implements SellerService {

    private SellerRepository sellerRepository;

    private ModelMapper mapper;


    public SellerServiceImpl(SellerRepository sellerRepository, ModelMapper mapper) {
        this.sellerRepository = sellerRepository;
        this.mapper = mapper;
    }

    public Optional<Seller> getSeller(Integer seller_id){
        return sellerRepository.findById(seller_id);
    }

}
