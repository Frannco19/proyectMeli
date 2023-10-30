package com.msmeli.service.services;

import com.msmeli.model.Item;
import com.msmeli.model.Stock;

public interface CostService {
    Item createProductsCosts(Item item, Stock stock);
}
