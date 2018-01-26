/**
 * 
 */
package idv.mint.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

import idv.mint.entity.enums.OverseasType;

/**
 * @author WuJerry
 *
 */
@Entity
@Table(name = "T_STOCK")
public class StockEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "STOCK_ID")
    private String stockId;

    @Column(name = "STOCK_NAME",nullable=false,length=100)
    private String stockName;

    @Column(name = "OVERSEAS_TYPE",nullable=false)
    private Integer overseasType;

    @Column(name = "STOCK_CATEGORY_ID")
    private Integer stockCategoryId;

    public StockEntity() {
    }

    public StockEntity(String stockId,String stockName,OverseasType overseasType,Integer stockCategoryId) {
	this.stockId = stockId;
	this.stockName = stockName;
	this.overseasType = overseasType.getValue();
	this.stockCategoryId = stockCategoryId;
    }
    
    public String getStockCode() {
	return stockId;
    }
    
    public String getStockId() {
	return stockId;
    }
    

    public void setStockId(String stockId) {
	this.stockId = stockId;
    }

    public String getStockName() {
	return stockName;
    }

    public void setStockName(String stockName) {
	this.stockName = stockName;
    }

    public Integer getOverseasType() {
	return overseasType;
    }

    public void setOverseasType(Integer overseasType) {
	this.overseasType = overseasType;
    }

    public Integer getStockCategoryId() {
	return stockCategoryId;
    }

    public void setStockCategoryId(Integer stockCategoryId) {
	this.stockCategoryId = stockCategoryId;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }

}
