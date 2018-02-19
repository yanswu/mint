package idv.mint.service;

import java.util.List;

import idv.mint.bean.Stock;
import idv.mint.bean.StockCategory;
import idv.mint.bean.StockSheet;

public interface StockService {
    
    public void saveStockEntities(List<Stock> stockList);

    public void saveStockSheetEntities(List<StockSheet> stockSheetList);
    
    public void saveStockCategoryEntities(List<StockCategory> list);
    
    public Stock getStock(String stockCode);

}
