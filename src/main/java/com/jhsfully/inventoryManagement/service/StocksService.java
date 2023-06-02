package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.StocksDto;
import com.jhsfully.inventoryManagement.exception.ProductException;
import com.jhsfully.inventoryManagement.exception.StocksException;
import com.jhsfully.inventoryManagement.model.StocksEntity;
import com.jhsfully.inventoryManagement.repository.ProductRepository;
import com.jhsfully.inventoryManagement.repository.StocksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.jhsfully.inventoryManagement.type.ProductErrorType.PRODUCT_NAME_NULL;
import static com.jhsfully.inventoryManagement.type.ProductErrorType.PRODUCT_NOT_FOUND;
import static com.jhsfully.inventoryManagement.type.StocksErrorType.*;

@Service
@AllArgsConstructor
public class StocksService implements StocksInterface {
    private final StocksRepository stocksRepository;
    private final ProductRepository productRepository;

    //쿼리 증명을 아직 하지 않았음!
    @Override
    public List<StocksDto.StockResponse> getAllStocks() {
        return stocksRepository.findStockGroupPid();
    }

    @Override
    public List<StocksDto.StockResponseLot> getLotByPid(Long pid) {
        return stocksRepository.findByPid(pid)
                .stream()
                .map(StocksEntity::toLotDto)
                .collect(Collectors.toList());
    }

    @Override
    public StocksDto.StockResponseLot addStock(StocksDto.StockAddRequest request) {

        validateAddStock(request);

        StocksEntity stocksEntity = StocksEntity.builder()
                .pid(request.getPid())
                .amount(request.getAmount())
                .lot(request.getLot())
                .company(request.getCompany())
                .build();

        return StocksEntity.toLotDto(stocksRepository.save(stocksEntity));
    }

    @Override
    public void spendStockById(Long id, Double amount) {
        StocksEntity stocksEntity = stocksRepository.findById(id)
                .orElseThrow(() -> new StocksException(STOCKS_NOT_FOUND));

        if(amount == null){
            throw new StocksException(STOCKS_AMOUNT_NULL);
        }

        if(amount <= 0){
            throw new StocksException(STOCKS_CANT_SPEND_OR_LESS_ZERO);
        }

        stocksEntity.spendAmount(amount);

        stocksRepository.save(stocksEntity);
    }

    @Override
    public void deleteStock(Long id) {
        //구현 보류(출고 서비스가 발생하여야 구현 가능)
    }

    //======================== Validates ======================================
    public void validateAddStock(StocksDto.StockAddRequest request){
        if(request.getPid() == null){
            throw new ProductException(PRODUCT_NAME_NULL);
        }

        boolean exists = productRepository.existsById(request.getPid());
        if(!exists){
            throw new ProductException(PRODUCT_NOT_FOUND);
        }

        if(request.getAmount() == null){
            throw new StocksException(STOCKS_AMOUNT_NULL);
        }

        if(request.getAmount() <= 0){
            throw new StocksException(STOCKS_NOT_CREATE_OR_LESS_ZERO);
        }
    }
}