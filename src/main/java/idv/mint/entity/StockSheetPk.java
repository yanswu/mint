/**
 * 
 */
package idv.mint.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author WuJerry
 *
 */
@Embeddable
public class StockSheetPk implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "STOCK_ID")
    private String stockId;

    @Column(name = "CAL_YEAR")
    private LocalDate calYear;

    public StockSheetPk() {
    }
    
    public StockSheetPk(String stockId,LocalDate calYear) {
	this.stockId = stockId;
	this.calYear = calYear;
    }

    public String getStockId() {
	return stockId;
    }

    public void setStockId(String stockId) {
	this.stockId = stockId;
    }

    public LocalDate getCalYear() {
	return calYear;
    }

    public void setCalYear(LocalDate calYear) {
	this.calYear = calYear;
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(this.stockId).append(this.calYear).hashCode();
    }

    @Override
    public boolean equals(Object obj) {

	if (obj == null)
	    return false;
	if (obj == this)
	    return true;

	if (obj instanceof StockSheetPk) {
	    StockSheetPk otherPk = (StockSheetPk) obj;
	    return new EqualsBuilder().append(this.stockId, otherPk.getStockId()).append(this.calYear, otherPk.getCalYear()).isEquals();
	}
	return false;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}
