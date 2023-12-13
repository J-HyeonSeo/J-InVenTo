package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.StocksDto;

import java.util.List;

public interface StocksInterface {
    StocksDto.StockResponseLot getStock(Long id);

    List<StocksDto.StockGroupResponse> getAllStocks();

    List<StocksDto.StockResponseLot> getLotByPid(Long productid);

    StocksDto.StockResponseLot addStock(StocksDto.StockAddRequest request);

    void setInbound(Long stockId, Long inboundId);

    void releaseInbound(Long stockId);

    void spendStockById(Long id, Double amount);

    void cancelSpendStockById(Long id, Double amount);

    void deleteStock(Long id);

}
