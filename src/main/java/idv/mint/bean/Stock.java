package idv.mint.bean;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Stock {

    private String stockCode;

    private String stockName;

    private StockCategory stockCategory;

    private List<StockSheet> epsSheetList;
    
    public Stock() {
    }
    
    public Stock(String stockCode,String stockName) {
	this.stockCode = stockCode;
	this.stockName = stockName;
    }
    
    public String getStockCode() {
	return stockCode;
    }

    public void setStockCode(String stockCode) {
	this.stockCode = stockCode;
    }

    public String getStockName() {
	return stockName;
    }

    public void setStockName(String stockName) {
	this.stockName = stockName;
    }

    public StockCategory getStockCategory() {
	return stockCategory;
    }

    public void setStockCategory(StockCategory stockCategory) {
	this.stockCategory = stockCategory;
    }

    public List<StockSheet> getEpsSheetList() {
	return epsSheetList;
    }

    public void setEpsSheetList(List<StockSheet> epsSheetList) {
	this.epsSheetList = epsSheetList;
    }
    
    
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
