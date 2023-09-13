package com.msmeli.service.implement;

import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.model.Item;
import com.msmeli.model.Seller;
import com.msmeli.repository.ItemRepository;
import com.msmeli.service.services.ItemService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;

    private ModelMapper mapper;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, ModelMapper mapper) {
        this.itemRepository = itemRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ItemResponseDTO> getSellerItems(Integer seller_id){
        List<Item> itemList = itemRepository.findAllBySeller_id(seller_id);
        return itemList
                .stream()
                .map(item -> mapper.map(item, ItemResponseDTO.class))
                .collect(Collectors.toList());
    }

}
