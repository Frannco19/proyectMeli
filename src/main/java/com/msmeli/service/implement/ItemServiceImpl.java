package com.msmeli.service.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.msmeli.dto.response.OneProductResponseDTO;
import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.dto.SellerDTO;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Item;
import com.msmeli.repository.ItemRepository;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.ItemService;
import com.msmeli.service.services.SellerService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final MeliFeignClient meliFeignClient;

    private final SellerService sellerService;

    private final MeliService meliService;

    private final ModelMapper mapper;

    public ItemServiceImpl(ItemRepository itemRepository, MeliFeignClient meliFeignClient, SellerService sellerService, MeliService meliService, ModelMapper mapper) {
        this.itemRepository = itemRepository;
        this.meliFeignClient = meliFeignClient;
        this.sellerService = sellerService;
        this.meliService = meliService;
        this.mapper = mapper;
    }

    @Override
    public List<ItemResponseDTO> getSellerItems(Integer sellerId){
        List<Item> itemList = itemRepository.getItemsBySellerId(sellerId);
        return itemList.stream().map(item -> mapper.map(item, ItemResponseDTO.class))
                .collect(Collectors.toList());
    }

//    @Override
//    public List<ItemResponseDTO> getCatalogItems(String productId) {
//        List<Item> catalogItems = itemRepository.getCatalogItems(productId);
//        return null;
////        return getItemResponseDTOS(catalogItems);
//    }

    @Override
    public OneProductResponseDTO getOneProduct(String productId){
        Item item = itemRepository.findByProductId(productId);
        SellerDTO seller = meliFeignClient.getSellerBySellerId(item.getSellerId());
        OneProductResponseDTO responseDTO = mapper.map(item, OneProductResponseDTO.class);
        responseDTO.setSeller_nickname(seller.getSeller().getNickname());
        responseDTO.setBestSellerPosition(meliService.getBestSellerPosition(item.getId(), productId));
        responseDTO.setCatalog_position(meliService.getCatalogPosition(item.getId(), productId));
        return responseDTO;
    }

}
