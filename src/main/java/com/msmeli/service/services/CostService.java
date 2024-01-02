package com.msmeli.service.services;

import com.msmeli.exception.AppException;
import com.msmeli.model.Item;
import com.msmeli.model.Stock;

public interface CostService {
    Item createProductsCosts(Item item, Stock stock);

    Item createProductsCostsV2(Item item, Stock lastBySku) throws AppException;
}
