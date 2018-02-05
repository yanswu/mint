package idv.mint.service;

import java.util.List;

import idv.mint.bean.Stock;

public interface StockService {
    
    public void saveStockEntities(List<Stock> stockList);
}
