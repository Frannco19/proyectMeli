package com.msmeli.service.implement;

import com.msmeli.dto.response.CostResponseDTO;
import com.msmeli.dto.response.OneProductResponseDTO;
import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.dto.SellerDTO;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Item;
import com.msmeli.repository.ItemRepository;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.*;
import com.msmeli.util.GrossIncome;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final MeliFeignClient meliFeignClient;

    private final ListingTypeService listingTypeService;

    private final MeliService meliService;

    private final ModelMapper mapper;

    private final StockService stockService;

    private final CostService costService;

    public ItemServiceImpl(ItemRepository itemRepository, MeliFeignClient meliFeignClient, ListingTypeService listingTypeService, MeliService meliService, ModelMapper mapper, StockServiceImpl stockService, CostService costService) {
        this.itemRepository = itemRepository;
        this.meliFeignClient = meliFeignClient;
        this.listingTypeService = listingTypeService;
        this.meliService = meliService;
        this.mapper = mapper;
        this.stockService = stockService;
        this.costService = costService;
    }

    @Override
    public Page<ItemResponseDTO> getSellerItems(Integer sellerId, int offset, int pageSize) {
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<Item> itemPage = itemRepository.getItemsBySellerId(sellerId, pageable);
        return getItemResponseDTOS(pageable, itemPage);
    }

    @Override
    public Page<ItemResponseDTO> getCatalogItems(Integer sellerId, int offset, int pageSize) {
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<Item> itemPage = itemRepository.getCatalogItems(sellerId, pageable);
        return getItemResponseDTOS(pageable, itemPage);
    }

    private Page<ItemResponseDTO> getItemResponseDTOS(Pageable pageable, Page<Item> itemPage) {
        List<ItemResponseDTO> items = itemPage.getContent()
                .stream()
                .parallel()
                .map(item -> {
                    CostResponseDTO costResponseDTO = mapper.map(item.getCost(), CostResponseDTO.class);
                    costResponseDTO.setIIBB(GrossIncome.IIBB.iibPercentage * 100);
                    ItemResponseDTO itemResponseDTO = mapper.map(item, ItemResponseDTO.class);
                    itemResponseDTO.setItem_cost(costResponseDTO);
                    String listingTypeName = listingTypeService.getListingTypeName(item.getListing_type_id());
                    itemResponseDTO.setListing_type_id(listingTypeName);
                    itemResponseDTO.setTotal_stock(stockService.getTotalStockBySku(item.getSku()));
                    return itemResponseDTO;
                })
                .toList();
        return new PageImpl<>(items, pageable, itemPage.getTotalElements());
    }

    public List<ItemResponseDTO> getItems() {
        return itemRepository.findAll().stream().map(item -> {
            CostResponseDTO costResponseDTO = mapper.map(item.getCost(), CostResponseDTO.class);
            costResponseDTO.setIIBB(GrossIncome.IIBB.iibPercentage * 100);
            ItemResponseDTO itemResponseDTO = mapper.map(item, ItemResponseDTO.class);
            itemResponseDTO.setItem_cost(costResponseDTO);
            String listingTypeName = listingTypeService.getListingTypeName(item.getListing_type_id());
            itemResponseDTO.setListing_type_id(listingTypeName);
            itemResponseDTO.setTotal_stock(stockService.getTotalStockBySku(item.getSku()));
            return itemResponseDTO;
        }).toList();
    }

    @Override
    public OneProductResponseDTO getOneProduct(String productId) {
        Item item = itemRepository.findByProductId(productId);
        SellerDTO seller = meliFeignClient.getSellerBySellerId(item.getSellerId());
        OneProductResponseDTO responseDTO = mapper.map(item, OneProductResponseDTO.class);
        responseDTO.setSeller_nickname(seller.getSeller().getNickname());
        responseDTO.setBestSellerPosition(meliService.getBestSellerPosition(item.getId(), productId));
        responseDTO.setCatalog_position(meliService.getCatalogPosition(item.getId(), productId));
        return responseDTO;
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }


    @EventListener(ApplicationReadyEvent.class)
    @Order(8)
    public void createProductsCosts() {
        List<Item> items = findAll();
        items.parallelStream().forEach((item -> {
            save(costService.createProductsCosts(item, stockService.findLastBySku(item.getSku())));
        }));
    }

    @Override
    public List<Item> searchProducts(String searchType, String searchInput, Pageable pageable) {
        List<Item> results;

        if ("sku".equals(searchType)) {
            results = itemRepository.findBySkuContaining(searchInput);
        } else if ("id".equals(searchType)) {
            results = itemRepository.findByIdContaining(searchInput);
//        } else if ("publicationNumber".equals(searchType)) {
//            results = itemRepository.findByPublicationNumberContaining(searchInput);
        } else {
            results = new ArrayList<>(); // Maneja el caso en el que el tipo de búsqueda no coincide
        }

        return results;
    }
}
