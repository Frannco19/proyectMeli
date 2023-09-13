package com.msmeli.service.services;

import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.model.Seller;

import java.util.List;

public interface ItemService {

    public List<ItemResponseDTO> getSellerItems(Integer seller_id);

}
