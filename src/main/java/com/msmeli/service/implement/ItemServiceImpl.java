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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<ItemResponseDTO> getSellerItems(Integer sellerId, int offset, int pageSize){
        Pageable pageable = PageRequest.of(offset,pageSize);
        Page<Item> itemPage = itemRepository.getItemsBySellerId(sellerId, pageable);
        List<ItemResponseDTO> items = itemPage.getContent()
                .stream()
                .parallel()
                .map(item -> mapper.map(item, ItemResponseDTO.class))
                .toList();
        return new PageImpl<>(items,pageable,itemPage.getTotalElements());
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
