package com.msmeli.service.implement;

import com.msmeli.configuration.security.entity.UserEntityUserDetails;
import com.msmeli.dto.feign.ItemFeignDTO;
import com.msmeli.dto.feign.ItemIdsResponseDTO;
import com.msmeli.dto.response.BuyBoxWinnerResponseDTO;
import com.msmeli.dto.response.CostResponseDTO;
import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.dto.response.OneProductResponseDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.Item;
import com.msmeli.model.Seller;
import com.msmeli.model.SellerRefactor;
import com.msmeli.repository.ItemRepository;
import com.msmeli.repository.SellerRefactorRepository;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private static final double MIN_MARGIN = .1;
    private final SellerService sellerService;
    private final SellerRefactorRepository sellerRefactorRepository;
    public ItemServiceImpl(ItemRepository itemRepository, MeliFeignClient meliFeignClient, ListingTypeService listingTypeService, MeliService meliService, ModelMapper mapper, StockServiceImpl stockService, CostService costService, SellerService sellerService, SellerRefactorRepository sellerRefactorRepository) {
        this.itemRepository = itemRepository;
        this.meliFeignClient = meliFeignClient;
        this.listingTypeService = listingTypeService;
        this.meliService = meliService;
        this.mapper = mapper;
        this.stockService = stockService;
        this.costService = costService;
        this.sellerService = sellerService;
        this.sellerRefactorRepository = sellerRefactorRepository;
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

    @Override
    public Page<ItemResponseDTO> getItemsAndCostPaged(Integer id, int offset, int pageSize) throws ResourceNotFoundException {
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<Item> itemCost = itemRepository.findAllBySellerId(id, pageable);
        if (itemCost.getContent().isEmpty()) throw new ResourceNotFoundException("No hay items con esos parametros");
        return new PageImpl<>(itemCost.stream().map(this::getItemResponseDTO).toList(), pageable, itemCost.getTotalElements());
    }

    /**
     * "método de conveniencia o método de envoltura"
     * Realiza la operación compleja de Cargar en la base de datos los item de un Seller
     * al llamar a los siguientes métodos:
     * getItemId()
     * setItemAtributtes()
     * @throws ResourceNotFoundException (Tratar mejor los errores)
     */
    @Override
    public void saveAllItemForSeller() throws ResourceNotFoundException {
        Long idSeller = getAuthenticatedUserId();
        SellerRefactor seller = sellerService.findById(idSeller);
        List<String> idsItems = getItemId(seller);
        setItemAtributtes(idsItems,seller);
    }

    /**
     * Este metodo se encarga de cargar todos los atributos de un item consultando a la api de mercadolibre recorriendo un
     * ArrayList de ids de item
     * @param idsItems ids de Item de un selle
     * @param seller Entidad "Seller" con la que se establece la relacion en la BD
     */
    private void setItemAtributtes(List<String>idsItems,SellerRefactor seller) {
        ItemFeignDTO itemRespose = null;
        List<Item>itemList = new ArrayList<>();
        for (String id : idsItems){
            itemRespose = meliFeignClient.getItemAtributtesRe(id);
            Item item = mapper.map(itemRespose, Item.class);
            item.setSellerRefactor(seller);
            itemList.add(item);
        }
        seller.setItems(itemList);
        sellerRefactorRepository.save(seller);
    }

    /**
     * Este metodo se encarga de consultar a la api de mercadolibre  traer todos los Ids de los items de un seller
     * @param seller entidad con las credenciasles necesarias para la consulta a la api de mercadolibre
     * @return idsItems ArrayList con los Ids de todos los items del seller
     */
    private List<String> getItemId(SellerRefactor seller) {
       int offset = 0;
       int limit = 100;
       ItemIdsResponseDTO resultado = null;
       List<String>idsItems = new ArrayList<>();
       do {

               resultado = meliFeignClient.getAllIDsForSeller(seller.getMeliID(),"Bearer " + seller.getTokenMl());
               idsItems.addAll(resultado.getResults());
               offset += limit;
       }while (offset < resultado.getPaging().getLimit() );

       return idsItems;

    }

    @Override
    public OneProductResponseDTO getOneProduct(String productId) throws ResourceNotFoundException {
        Item item = itemRepository.findByProductId(productId);
        Seller seller = sellerService.findBySellerId(Long.valueOf(item.getSellerId()));

        OneProductResponseDTO responseDTO = mapper.map(item, OneProductResponseDTO.class);
        responseDTO.setSeller_nickname(seller.getNickname());

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

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserEntityUserDetails) {
            UserEntityUserDetails userDetails = (UserEntityUserDetails) authentication.getPrincipal();
            return userDetails.getId();
        } else {
            return null;
        }
    }
}
