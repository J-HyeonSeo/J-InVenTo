package com.jhsfully.inventoryManagement.service;

import com.jhsfully.inventoryManagement.dto.StocksDto;

import java.util.List;

public interface StocksInterface {

    public List<StocksDto.StockResponse> getAllStocks();

    public List<StocksDto.StockResponseLot> getLotByPid(Long pid);

    public StocksDto.StockResponseLot addStock(StocksDto.StockAddRequest request);

    public void spendStockById(Long id);

    public void deleteStock(Long id);

}
