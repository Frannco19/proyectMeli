package com.msmeli.service.implement;

import com.msmeli.dto.feign.ItemFeignDTO;
import com.msmeli.dto.feign.ItemIdsResponseDTO;
import com.msmeli.dto.response.BuyBoxWinnerResponseDTO;
import com.msmeli.dto.response.CostResponseDTO;
import com.msmeli.dto.response.ItemResponseDTO;
import com.msmeli.dto.response.OneProductResponseDTO;
import com.msmeli.exception.AppException;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.feignClient.MeliFeignClient;
import com.msmeli.model.*;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    private final UserEntityService userEntityService;
    private final SellerRefactorRepository sellerRefactorRepository;
    public ItemServiceImpl(ItemRepository itemRepository, MeliFeignClient meliFeignClient, ListingTypeService listingTypeService, MeliService meliService, ModelMapper mapper, StockServiceImpl stockService, CostService costService, SellerService sellerService, UserEntityService userEntityService, SellerRefactorRepository sellerRefactorRepository) {
        this.itemRepository = itemRepository;
        this.meliFeignClient = meliFeignClient;
        this.listingTypeService = listingTypeService;
        this.meliService = meliService;
        this.mapper = mapper;
        this.stockService = stockService;
        this.costService = costService;
        this.sellerService = sellerService;
        this.userEntityService = userEntityService;
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
        Long idSeller = userEntityService.getAuthenticatedUserId();
        SellerRefactor seller = sellerService.findById(idSeller);
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<Item> itemCost = itemRepository.findAllBySellerRefactorIdPage(seller,pageable);
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
        Long idSeller = userEntityService.getAuthenticatedUserId();
        SellerRefactor seller = sellerService.findById(idSeller);
        List<String> idsItems = getItemId(seller);
        setItemAtributtes(idsItems,seller);
    }

    /**
     * Este metodo se encarga de cargar todos los atributos de un item consultando a la api de mercadolibre recorriendo un
     * ArrayList de ids de item
     * @param idsItems ids de Item de un seller
     * @param seller Entidad "Seller" con la que se establece la relacion en la BD
     */
    private void setItemAtributtes(List<String>idsItems,SellerRefactor seller) {
        List<Item>itemList = new ArrayList<>();
        for (String id : idsItems){
                ItemFeignDTO itemRespose = meliFeignClient.getItemAtributtesRe(id,"Bearer " + seller.getTokenMl());
                Item item = mapper.map(itemRespose, Item.class);
                item.setUpdated_date_item(LocalDateTime.now());
                item.setSellerRefactor(seller);
                itemList.add(item);
            }
        seller.setItems(itemList);
        sellerRefactorRepository.save(seller);
    }

    /**
     * Este metodo se encarga de consultar a la api de mercadolibre  traer todos los Ids de los items de un seller
     * @param seller entidad con las credenciales necesarias para la consulta a la api de mercadolibre
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
        //Seller seller = sellerService.findBySellerId(Long.valueOf(item.getSellerId()));

        OneProductResponseDTO responseDTO = mapper.map(item, OneProductResponseDTO.class);
       // responseDTO.setSeller_nickname(seller.getNickname());

        //responseDTO.setCatalog_position(meliService.getCatalogPosition(item.getId(), productId));
        responseDTO.setCatalog_position(item.getCatalog_position());
        return responseDTO;
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    /**
     * Metodo qye se encarga de buscar todos los Items de un Seller
     * @param idSeller id del seller a buscar
     * @return List<Item> lista de items relacionados al id
     * @throws AppException en caso de no encontrar nada lanza la exception
     */
    @Override
    public  List<Item> findAllidSeller(Long idSeller) throws AppException {
        List<Item> listOfItems = itemRepository.findAllBySellerRefactorId(idSeller);
        if(listOfItems.isEmpty()){
            throw new AppException("No Content","metodo: findAllidSeller",  000, 204);
        }
        return listOfItems;
    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    /**
     * Metodo se encarga de buscar todos los items  e invocar por cada uno la funcion
     * createProductsCost
     */
    public void createProductsCosts() {
        List<Item> items = findAll();
        items.parallelStream().forEach((item -> {
            try {
                save(costService.createProductsCostsV2(item, stockService.findLastBySku(item.getSku())));
            } catch (AppException e) {
                throw new RuntimeException(e);
            }
        }
        ));
    }

    /**
     * Este metodo se encarga de buscar los items de un seller con diferentes filtros segun nuestra logica de negocio
     * @param searchType  String con el tipo de busqueda "sku" o "mla"
     * @param searchInput String que llega desde el controlador
     * @param offset El desplazamiento o posición en relación con el principio.
     *               Para listas o secuencias, este valor representa el índice del elemento.
     *               Para consultas SQL, indica el número de filas que se deben omitir desde el principio.
     *               El valor debe ser mayor o igual a cero.
     * @param pageSize Tamñano de pagina solicitado desde el controlador
     * @param isCatalogue Boolean
     * @param isActive String Solicitado si un item es activo o no
     * @return Page < ItemResponseDTO > Page de ItemResposeDTO cargado con los items
     * @throws ResourceNotFoundException
     */
    @Override
    public Page<ItemResponseDTO> searchProducts(String searchType, String searchInput, int offset, int pageSize, boolean isCatalogue, String isActive) throws ResourceNotFoundException, AppException {
        Long idSeller = userEntityService.getAuthenticatedUserId();
        SellerRefactor seller = sellerService.findById(idSeller);
        Pageable pageable = PageRequest.of(offset, pageSize);
        Page<Item> results = itemRepository.findByFilters("%" + searchInput.toUpperCase() + "%", searchType, isCatalogue, isActive,seller, pageable);
        if (results.getContent().isEmpty()) throw new AppException("No Content","/search->searchProduct",  000, 204);
        return results.map(item -> {
            ItemResponseDTO itemDTO = getItemResponseDTO(item);
            //TODO No se puede tratar si no tenemos costos y product-ID
           // itemDTO = calculateColor(itemDTO);
            return itemDTO;
        });
    }


    /**
     * Metodo que se encarga de llamar a todos los metodos relacionados para cargar
     * ItemResposeDto con los datos solicitados por el Front
     * @param item Entidad Item previamente cargada desde BD
     * @return ItemResposeDTO DTO con los datos solicitados
     */
    @NotNull
    private ItemResponseDTO getItemResponseDTO(Item item) {
        ItemResponseDTO itemResponseDTO = mapper.map(item, ItemResponseDTO.class);
        if(item.getCost() != null) {
            CostResponseDTO costResponseDTO = mapper.map(item.getCost(), CostResponseDTO.class);
            costResponseDTO.setIIBB(GrossIncome.IIBB.iibPercentage * 100);
            itemResponseDTO.setItem_cost(costResponseDTO);
        }
        //String listingTypeName = listingTypeService.getListingTypeName(item.getListing_type_id());
        //itemResponseDTO.setListing_type_id(listingTypeName);
        itemResponseDTO.setTotal_stock(stockService.getTotalStockBySku(item.getSku()));
        return itemResponseDTO;
    }

    /**
     * Este metodo se encarga de calcular el color del semaforo para el front
     * @param itemResponseDTO DTO de itemResponseDTO donde se cargara el dato
     * @return devuelve el Dto cargado
     */
    private ItemResponseDTO calculateColor(ItemResponseDTO itemResponseDTO) throws ResourceNotFoundException {
        BuyBoxWinnerResponseDTO firstPlace = null;
        double winnerPrice = 0.0;
        double adjustedPrice = 0.0;
        TrafficLight trafficLight = null;
        itemResponseDTO.setCatalog_position(meliService.getCatalogPosition(itemResponseDTO.getId(), itemResponseDTO.getCatalog_product_id()));
        if (itemResponseDTO.getCatalog_product_id() != null && itemResponseDTO.getCatalog_position() != -1) {
            firstPlace = meliService.getBuyBoxWinnerCatalog(itemResponseDTO.getCatalog_product_id());
            winnerPrice = itemResponseDTO.getCatalog_position() >= 0 ? firstPlace.getPrice() : 0.0;
            adjustedPrice = (itemResponseDTO.getItem_cost().getReplacement_cost() + itemResponseDTO.getItem_cost().getShipping()) / (1 - ((itemResponseDTO.getItem_cost().getComision_fee() / 100 + 0.045) + MIN_MARGIN));
            if (firstPlace.getSeller_id() == 1152777827) trafficLight = TrafficLight.GREEN;
            else if (adjustedPrice <= winnerPrice) trafficLight = TrafficLight.YELLOW;
            else trafficLight = TrafficLight.RED;
            itemResponseDTO.setTrafficLight(trafficLight);
            itemResponseDTO.setWinnerPrice(winnerPrice);
        }
        return itemResponseDTO;
    }

}
