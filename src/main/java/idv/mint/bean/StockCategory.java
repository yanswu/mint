package idv.mint.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import idv.mint.entity.enums.StockMarketType;

public class StockCategory {

    private StockMarketType marketType;

    private Integer sequence;

    private String name;

    public StockCategory() {
    }

    public StockCategory(StockMarketType marketType, Integer sequence,String name) {
	this.marketType = marketType;
	this.sequence = sequence;
	this.name = name;
    }

    public StockMarketType getMarketType() {
	return marketType;
    }

    public void setMarketType(StockMarketType marketType) {
	this.marketType = marketType;
    }

    public Integer getSequence() {
	return sequence;
    }

    public void setSequence(Integer sequence) {
	this.sequence = sequence;
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
