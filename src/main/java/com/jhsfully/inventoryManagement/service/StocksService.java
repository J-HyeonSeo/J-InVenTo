package com.jhsfully.inventoryManagement.service;

import static com.jhsfully.inventoryManagement.type.InboundErrorType.INBOUND_NOT_FOUND;
import static com.jhsfully.inventoryManagement.type.ProductErrorType.PRODUCT_NOT_FOUND;
import static com.jhsfully.inventoryManagement.type.StocksErrorType.STOCKS_AMOUNT_NULL;
import static com.jhsfully.inventoryManagement.type.StocksErrorType.STOCKS_CANT_CANCEL_OR_LESS_ZERO;
import static com.jhsfully.inventoryManagement.type.StocksErrorType.STOCKS_CANT_SPEND_OR_LESS_ZERO;
import static com.jhsfully.inventoryManagement.type.StocksErrorType.STOCKS_NOT_CREATE_OR_LESS_ZERO;
import static com.jhsfully.inventoryManagement.type.StocksErrorType.STOCKS_NOT_FOUND;

import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.entity.InboundEntity;
import com.jhsfully.inventoryManagement.entity.ProductEntity;
import com.jhsfully.inventoryManagement.entity.StocksEntity;
import com.jhsfully.inventoryManagement.exception.InboundException;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.exception.StocksException;
import com.jhsfully.inventoryManagement.lock.ProcessLock;
import com.jhsfully.inventoryManagement.repository.InboundRepository;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
import com.jhsfully.inventoryManagement.repository.StocksRepository;
import com.jhsfully.inventoryManagement.type.LockType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class StocksService implements StocksInterface {
    private final StocksRepository stocksRepository;
    private final ProductRepository productRepository;
    private final InboundRepository inboundRepository;

    @Override
    @Transactional(readOnly = true)
    public StocksDto.StockResponseLot getStock(Long id){
        return StocksEntity.toLotDto(stocksRepository.findById(id)
                .orElseThrow(() -> new StocksException(STOCKS_NOT_FOUND)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StocksDto.StockGroupResponse> getAllStocks() {
        return stocksRepository.getStocksGroupProduct();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StocksDto.StockResponseLot> getLotByPid(Long productid) {

        ProductEntity product = productRepository.findById(productid)
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        return stocksRepository.findByProduct(product)
                .stream()
                .map(StocksEntity::toLotDto)
                .collect(Collectors.toList());
    }

    @Override
    public StocksDto.StockResponseLot addStock(StocksDto.StockAddRequest request) {

        ProductEntity productEntity = validateAddStock(request);

        StocksEntity stocksEntity = StocksEntity.builder()
                .product(productEntity)
                .amount(request.getAmount())
                .lot(request.getLot())
                .build();

        return StocksEntity.toLotDto(stocksRepository.save(stocksEntity));
    }

    @Override
    public void setInbound(Long stockId, Long inboundId){
        StocksEntity stock = stocksRepository.findById(stockId)
                .orElseThrow(() -> new StocksException(STOCKS_NOT_FOUND));

        InboundEntity inbound = inboundRepository.findById(inboundId)
                        .orElseThrow(() -> new InboundException(INBOUND_NOT_FOUND));

        stock.setInbound(inbound);
        stocksRepository.save(stock);
    }

    @Override
    public void releaseInbound(Long stockId){
        StocksEntity stock = stocksRepository.findById(stockId)
                .orElseThrow(() -> new StocksException(STOCKS_NOT_FOUND));
        stock.setInbound(null);
        stocksRepository.save(stock);
    }

    @Override
    @ProcessLock(group = LockType.INBOUND_OUTBOUND, key = "#id")
    public void spendStockById(Long id, Double amount) {
        StocksEntity stocksEntity = validateSpendStock(id, amount);

        stocksEntity.spendAmount(amount);

        stocksRepository.save(stocksEntity);
    }

    @Override
    @ProcessLock(group = LockType.INBOUND_OUTBOUND, key = "#id")
    public void cancelSpendStockById(Long id, Double amount){
        StocksEntity stocksEntity = validateCancelSpendStock(id, amount);

        stocksEntity.cancelSpendAmount(amount);

        stocksRepository.save(stocksEntity);
    }

    @Override
    public void deleteStock(Long id) {
        //파서드단에서 밸리데이션을 수행했을거임.
        StocksEntity stocksEntity = stocksRepository.findById(id)
                .orElseThrow(() -> new StocksException(STOCKS_NOT_FOUND));
        stocksRepository.delete(stocksEntity);
    }

    //======================== Validates ======================================
    private ProductEntity validateAddStock(StocksDto.StockAddRequest request){

        if(request.getAmount() == null){
            throw new StocksException(STOCKS_AMOUNT_NULL);
        }

        if(request.getAmount() <= 0){
            throw new StocksException(STOCKS_NOT_CREATE_OR_LESS_ZERO);
        }

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));

        return product;
    }

    private StocksEntity validateSpendStock(Long id, Double amount){

        if(amount == null){
            throw new StocksException(STOCKS_AMOUNT_NULL);
        }

        if(amount <= 0){
            throw new StocksException(STOCKS_CANT_SPEND_OR_LESS_ZERO);
        }

        StocksEntity stocksEntity = stocksRepository.findById(id)
                .orElseThrow(() -> new StocksException(STOCKS_NOT_FOUND));

        return stocksEntity;
    }

    private StocksEntity validateCancelSpendStock(Long id, Double amount){

        if(amount == null){
            throw new StocksException(STOCKS_AMOUNT_NULL);
        }

        if(amount <= 0){
            throw new StocksException(STOCKS_CANT_CANCEL_OR_LESS_ZERO);
        }

        StocksEntity stocksEntity = stocksRepository.findById(id)
                .orElseThrow(() -> new StocksException(STOCKS_NOT_FOUND));

        return stocksEntity;
    }
}
