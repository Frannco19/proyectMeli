package com.msmeli.service.implement;

import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.repository.ItemRepository;
import com.msmeli.service.services.ItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;

    private ModelMapper mapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, ModelMapper mapper) {
        this.itemRepository = itemRepository;
        this.mapper = mapper;
    }

    public List<ItemResponseDTO> getSellerItems(){
        
    }

}
