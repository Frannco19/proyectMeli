package com.msmeli.service.implement;

import com.msmeli.dto.response.OneProductResponseDTO;
import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.dto.SellerDTO;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Item;
import com.msmeli.repository.ItemRepository;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.CostService;
import com.msmeli.service.services.ItemService;
import com.msmeli.service.services.ListingTypeService;
import com.msmeli.service.services.SellerService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final MeliFeignClient meliFeignClient;

    private final SellerService sellerService;

    private final ListingTypeService listingTypeService;

    private final MeliService meliService;

    private final ModelMapper mapper;

    public ItemServiceImpl(ItemRepository itemRepository, MeliFeignClient meliFeignClient, SellerService sellerService, ListingTypeService listingTypeService, MeliService meliService, ModelMapper mapper) {
        this.itemRepository = itemRepository;
        this.meliFeignClient = meliFeignClient;
        this.sellerService = sellerService;
        this.listingTypeService = listingTypeService;
        this.meliService = meliService;
        this.mapper = mapper;
    }

    @Override
    public Page<ItemResponseDTO> getSellerItems(Integer sellerId, int offset, int pageSize){
        Pageable pageable = PageRequest.of(offset,pageSize);
        Page<Item> itemPage = itemRepository.getItemsBySellerId(sellerId, pageable);
        return getItemResponseDTOS(pageable, itemPage);
    }

    @Override
    public Page<ItemResponseDTO> getCatalogItems(Integer sellerId, int offset, int pageSize){
        Pageable pageable = PageRequest.of(offset,pageSize);
        Page<Item> itemPage = itemRepository.getCatalogItems(sellerId, pageable);
        return getItemResponseDTOS(pageable, itemPage);
    }

    private Page<ItemResponseDTO> getItemResponseDTOS(Pageable pageable, Page<Item> itemPage) {
        List<ItemResponseDTO> items = itemPage.getContent()
                .stream()
                .parallel()
                .map(item -> {
                    ItemResponseDTO itemResponseDTO = mapper.map(item, ItemResponseDTO.class);
                    String listingTypeName = listingTypeService.getListingTypeName(item.getListing_type_id());
                    itemResponseDTO.setListing_type_id(listingTypeName);
                    return itemResponseDTO;
                })
                .toList();
        return new PageImpl<>(items,pageable,itemPage.getTotalElements());
    }

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

    public List<Item> findAll(){
        return itemRepository.findAll();
    }

}
