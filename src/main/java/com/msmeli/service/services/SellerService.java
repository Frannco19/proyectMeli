package com.msmeli.service.services;

import com.msmeli.model.Seller;

import java.util.Optional;

public interface SellerService {

    public Optional<Seller> getSeller(Integer seller_id);
}
