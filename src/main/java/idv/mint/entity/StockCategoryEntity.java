package idv.mint.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "T_STOCK_CATEGORY")
public class StockCategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "STOCK_CATEGORY_ID")
    private Integer stockCategoryId;

    @Column(name = "MARKET_TYPE", nullable = false)
    private Integer marketType;

    @Column(name = "CATEGORY_NAME", nullable = false, length = 100)
    private String categoryName;

    @Column(name = "ORDER_NO")
    private Integer orderNo;

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "STOCK_CATEGORY_ID")
    private List<StockEntity> stockList;

    public StockCategoryEntity() {
    }

    public Integer getStockCategoryId() {
	return stockCategoryId;
    }

    public void setStockCategoryId(Integer stockCategoryId) {
	this.stockCategoryId = stockCategoryId;
    }

    public Integer getMarketType() {
	return marketType;
    }

    public void setMarketType(Integer marketType) {
	this.marketType = marketType;
    }

    public String getCategoryName() {
	return categoryName;
    }

    public void setCategoryName(String categoryName) {
	this.categoryName = categoryName;
    }

    public Integer getOrderNo() {
	return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
	this.orderNo = orderNo;
    }

    public List<StockEntity> getStockList() {
	return stockList;
    }

    public void setStockList(List<StockEntity> stockList) {
	this.stockList = stockList;
    }
    
    public void addStockEntity(StockEntity stockEntity) {
	if(this.stockList == null) {
	    stockList = new ArrayList<>();
	}
	stockList.add(stockEntity);
    }
    
    
    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }

}
