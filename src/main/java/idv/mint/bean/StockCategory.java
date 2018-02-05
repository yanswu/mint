package idv.mint.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import idv.mint.entity.enums.StockMarketType;

public class StockCategory {

    private String stockCategoryId;

    private StockMarketType marketType;

    private Integer orderNo;

    private String name;

    public StockCategory() {
    }

    public StockCategory(StockMarketType marketType, Integer orderNo, String name) {
	this.marketType = marketType;
	this.orderNo = orderNo;
	this.name = name;
    }

    public String getStockCategoryId() {
	return stockCategoryId;
    }

    public void setStockCategoryId(String stockCategoryId) {
	this.stockCategoryId = stockCategoryId;
    }

    public StockMarketType getMarketType() {
	return marketType;
    }

    public void setMarketType(StockMarketType marketType) {
	this.marketType = marketType;
    }

    public Integer getOrderNo() {
	return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
	this.orderNo = orderNo;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
