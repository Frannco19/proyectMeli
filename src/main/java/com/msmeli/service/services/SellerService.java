package com.msmeli.service.services;

import com.msmeli.model.Seller;

import java.util.Optional;

public interface SellerService {

     Optional<Seller> getSeller(Integer seller_id);
}
