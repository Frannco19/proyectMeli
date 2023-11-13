package com.msmeli.service.implement;

import com.msmeli.dto.response.BuyBoxWinnerResponseDTO;
import com.msmeli.dto.response.CostResponseDTO;
import com.msmeli.dto.response.OneProductResponseDTO;
import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.dto.SellerDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Item;
import com.msmeli.repository.ItemRepository;
import com.msmeli.service.feignService.MeliService;
import com.msmeli.service.services.*;
import com.msmeli.util.GrossIncome;
import com.msmeli.util.TrafficLight;
import org.jetbrains.annotations.NotNull;
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

    private final ListingTypeService listingTypeService;

    private final MeliService meliService;

    private final ModelMapper mapper;

    private final StockService stockService;

    private final CostService costService;

    private static final double MIN_MARGIN = .1;

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
        List<ItemResponseDTO> items = itemPage.getContent().stream().parallel().map(this::getItemResponseDTO).toList();
        return new PageImpl<>(items, pageable, itemPage.getTotalElements());
    }

    @Override
    public List<ItemResponseDTO> getItems() {
        return itemRepository.findAll().stream().map(this::getItemResponseDTO).toList();
    }

    public Page<ItemResponseDTO> getItemsAndCost(Integer id, int offset, int pageSize) {
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<Item> itemCost = itemRepository.findAllBySellerId(id, pageable);
        return itemRepository.findAll().stream().map(this::getItemResponseDTO).toList();
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


    public void createProductsCosts() {
        List<Item> items = findAll();
        items.parallelStream().forEach((item -> save(costService.createProductsCosts(item, stockService.findLastBySku(item.getSku())))
        ));
    }

    @Override
    public Page<ItemResponseDTO> searchProducts(String searchType, String searchInput, int offset, int pageSize, boolean isCatalogue, String isActive) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(offset, pageSize);
        int inCatalogue = isCatalogue ? -1 : -2;
        Page<Item> results = itemRepository.findByFilters("%" + searchInput.toUpperCase() + "%", searchType, inCatalogue, isActive, pageable);
        if (results.getContent().isEmpty()) throw new ResourceNotFoundException("No hay items con esos parametros");
        return results.map(item -> {
            ItemResponseDTO itemDTO = getItemResponseDTO(item);
            itemDTO = calculateColor(itemDTO);
            return itemDTO;
        });
    }

    @NotNull
    private ItemResponseDTO getItemResponseDTO(Item item) {
        CostResponseDTO costResponseDTO = mapper.map(item.getCost(), CostResponseDTO.class);
        costResponseDTO.setIIBB(GrossIncome.IIBB.iibPercentage * 100);
        ItemResponseDTO itemResponseDTO = mapper.map(item, ItemResponseDTO.class);
        itemResponseDTO.setItem_cost(costResponseDTO);
        String listingTypeName = listingTypeService.getListingTypeName(item.getListing_type_id());
        itemResponseDTO.setListing_type_id(listingTypeName);
        itemResponseDTO.setTotal_stock(stockService.getTotalStockBySku(item.getSku()));
        return itemResponseDTO;
    }

    private ItemResponseDTO calculateColor(ItemResponseDTO item) {
        BuyBoxWinnerResponseDTO firstPlace = null;
        double winnerPrice = 0.0;
        double adjustedPrice = 0.0;
        TrafficLight trafficLight = null;
        item.setCatalog_position(meliService.getCatalogPosition(item.getId(), item.getCatalog_product_id()));
        if (item.getCatalog_product_id() != null && item.getCatalog_position() != -1) {
            firstPlace = meliService.getBuyBoxWinnerCatalog(item.getCatalog_product_id());
            winnerPrice = item.getCatalog_position() >= 0 ? firstPlace.getPrice() : 0.0;
            adjustedPrice = (item.getItem_cost().getReplacement_cost() + item.getItem_cost().getShipping()) / (1 - ((item.getItem_cost().getComision_fee() / 100 + 0.045) + MIN_MARGIN));
            if (firstPlace.getSeller_id() == 1152777827) trafficLight = TrafficLight.GREEN;
            else if (adjustedPrice <= winnerPrice) trafficLight = TrafficLight.YELLOW;
            else trafficLight = TrafficLight.RED;
            item.setTrafficLight(trafficLight);
            item.setWinnerPrice(winnerPrice);
        }
        return item;
    }
}
