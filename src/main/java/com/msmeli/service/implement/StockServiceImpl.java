package com.msmeli.service.implement;

import com.msmeli.dto.StockDTO;
import com.msmeli.dto.request.StockRequestDTO;
import com.msmeli.exception.ResourceNotFoundException;
import com.msmeli.model.SellerRefactor;
import com.msmeli.model.Stock;
import com.msmeli.model.UserEntity;
import com.msmeli.repository.SellerRefactorRepository;
import com.msmeli.repository.StockRepository;
import com.msmeli.repository.UserEntityRepository;
import com.msmeli.service.services.StockService;
import com.msmeli.service.services.UserEntityService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final UserEntityRepository userEntityRepository;
    private final ModelMapper modelMapper;
    private final UserEntityService userEntityService;

    private final SellerRefactorRepository sellerRefactorRepository;

    /**
     * Servicio que gestiona las operaciones relacionadas con el stock.
     */
    public StockServiceImpl(StockRepository stockRepository, UserEntityRepository userEntityRepository, SellerRefactorRepository sellerRefactorRepository, UserEntityService userEntityService) {
        this.stockRepository = stockRepository;
        this.userEntityRepository = userEntityRepository;
        this.sellerRefactorRepository = sellerRefactorRepository;
        this.userEntityService =userEntityService;
        this.modelMapper = new ModelMapper();
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id El ID del usuario.
     * @return La entidad del usuario correspondiente al ID.
     * @throws EntityNotFoundException Si no se encuentra ningún usuario con el ID proporcionado.
     */
    @Override
    public UserEntity getUserById(Long id) {
        return userEntityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    /**
     * Guarda el stock del vendedor.
     *
     * Este método guarda la información del stock para un vendedor autenticado. Recupera el ID del usuario autenticado,
     * mapea los datos del DTO de la solicitud a objetos de tipo Stock, ajusta el identificador del vendedor y redondea
     * los precios antes de guardar la lista de stocks en el repositorio.
     *
     * @param requestDTO El DTO de la solicitud que contiene la información del stock a guardar.
     */
    @Override
    public void saveSellerStock(StockRequestDTO requestDTO) {

        Long authenticatedUserId = userEntityService.getAuthenticatedUserId();
        UserEntity user = getUserById(authenticatedUserId);

        List<Stock> stockList = requestDTO.getContent()
                .parallelStream()
                .map(e -> {
                    Stock sellerStock = modelMapper.map(e, Stock.class);
                    sellerStock.setSeller_id((SellerRefactor) user);
                    sellerStock.setPrice(Math.round(sellerStock.getPrice() * 100.0) /100.0);
                    return sellerStock;
                })
                .collect(Collectors.toList());

        stockRepository.saveAll(stockList);
    }

    /**
     * Encuentra la última existencia por SKU.
     *
     * @param sku El SKU de la existencia.
     * @return La existencia correspondiente al SKU.
     */
    @Override
    public Stock findLastBySku(String sku) {
        return stockRepository.findBySku(sku);
    }

    /**
     * Obtiene el total de existencias por SKU.
     *
     * @param sku El SKU de las existencias.
     * @return El total de existencias para el SKU.
     */
    @Override
    public Integer getTotalStockBySku(String sku) {
        return stockRepository.getTotalBySku(sku);
    }

    /**
     * Obtiene todas las existencias mapeadas para un vendedor.
     *
     * @param sellerId El ID del vendedor.
     * @return Lista de existencias mapeadas para el vendedor.
     */
    @Override
    public List<StockDTO> findAllMapped(Long sellerId) {
        return stockRepository.findAllBySellerId(sellerId).stream().map(stock -> modelMapper.map(stock, StockDTO.class)).toList();
    }

    /**
     * Obtiene todas las existencias para un vendedor.
     *
     * @param sellerId El ID del vendedor.
     * @return Lista de todas las existencias para el vendedor.
     * @throws ResourceNotFoundException Si el vendedor no tiene existencias.
     */
    @Override
    public List<Stock> findAll(Long sellerId) throws ResourceNotFoundException {
        List<Stock> stockList = stockRepository.findAllBySellerId(sellerId);
        if(stockList.isEmpty()) throw new ResourceNotFoundException("El seller buscado no posee stock");
        return stockList;
    }

    /**
     * Obtiene todas las existencias paginadas para un vendedor.
     *
     * @param sellerId El ID del vendedor.
     * @param pageable La información de paginación.
     * @return Página de existencias para el vendedor.
     * @throws ResourceNotFoundException Si el vendedor no tiene existencias.
     */
    public Page<Stock> findAllPaged(Long sellerId, Pageable pageable) throws ResourceNotFoundException {
        Page<Stock> stockList = stockRepository.findAllBySellerId(sellerId,pageable);
        if(stockList.getContent().isEmpty()) throw new ResourceNotFoundException("El seller buscado no posee stock");
        return stockList;
    }

    /**
     * Recupera y devuelve una lista de objetos StockDTO que representan el stock asociado al usuario autenticado.
     *
     * El método utiliza el servicio {@code userEntityService} para obtener el ID del usuario autenticado y
     * luego busca los stocks asociados a ese ID utilizando el repositorio {@code stockRepository}.
     *
     * @return Una lista de objetos StockDTO que representan el stock del usuario autenticado.
     */
    public Page<StockDTO> findAllByAuthenticatedUser(Pageable pageable) {
        // Recupera el ID del usuario autenticado
        Long authenticatedUserId = userEntityService.getAuthenticatedUserId();

        // Busca el stock por el ID del usuario autenticado
        Page<Stock> stockPage = stockRepository.findAllByUserId(authenticatedUserId, pageable);

        // Crea instancias de StockDTO a partir de Stock
        List<StockDTO> stockDTOList = stockPage.getContent().stream()
                .map(stock -> new StockDTO(
                        stock.getId(),
                        stock.getSku(),
                        stock.getAvailable_quantity(),
                        stock.getPrice(),
                        stock.getRegister_date(),

                        null
                ))
                .collect(Collectors.toList());

        return new PageImpl<>(stockDTOList, pageable, stockPage.getTotalElements());
    }

}
