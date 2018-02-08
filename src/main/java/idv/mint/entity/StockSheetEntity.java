package idv.mint.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "T_STOCK_SHEET")
public class StockSheetEntity {

    @EmbeddedId
    private StockSheetPk sheetPk;

    @Column(name = "EPS_Q1")
    private BigDecimal epsQ1;

    @Column(name = "EPS_Q2")
    private BigDecimal epsQ2;

    @Column(name = "EPS_Q3")
    private BigDecimal epsQ3;

    @Column(name = "EPS_Q4")
    private BigDecimal epsQ4;

    @Column(name = "CASH_DIVIDEND")
    private BigDecimal cashDividend;

    @Column(name = "STOCK_DIVIDEND")
    private BigDecimal stockDividend;

    public StockSheetEntity() {
    }

    public StockSheetPk getSheetPk() {
	return sheetPk;
    }

    public void setSheetPk(StockSheetPk sheetPk) {
	this.sheetPk = sheetPk;
    }

    public BigDecimal getEpsQ1() {
	return epsQ1;
    }

    public void setEpsQ1(BigDecimal epsQ1) {
	this.epsQ1 = epsQ1;
    }

    public BigDecimal getEpsQ2() {
	return epsQ2;
    }

    public void setEpsQ2(BigDecimal epsQ2) {
	this.epsQ2 = epsQ2;
    }

    public BigDecimal getEpsQ3() {
	return epsQ3;
    }

    public void setEpsQ3(BigDecimal epsQ3) {
	this.epsQ3 = epsQ3;
    }

    public BigDecimal getEpsQ4() {
	return epsQ4;
    }

    public void setEpsQ4(BigDecimal epsQ4) {
	this.epsQ4 = epsQ4;
    }

    public BigDecimal getCashDividend() {
	return cashDividend;
    }

    public void setCashDividend(BigDecimal cashDividend) {
	this.cashDividend = cashDividend;
    }

    public BigDecimal getStockDividend() {
	return stockDividend;
    }

    public void setStockDividend(BigDecimal stockDividend) {
	this.stockDividend = stockDividend;
    }

    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }
}