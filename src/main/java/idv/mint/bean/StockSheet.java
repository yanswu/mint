package idv.mint.bean;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class StockSheet {

    private String stockCode;

    private LocalDate baseDate;

    private BigDecimal epsQ1;

    private BigDecimal epsQ2;

    private BigDecimal epsQ3;

    private BigDecimal epsQ4;

    private BigDecimal cashDividend;

    private BigDecimal stockDividend;

    private BigDecimal totalEps;

    public StockSheet() {
    }

    public StockSheet(String stockCode, LocalDate baseDate) {
	this.stockCode = stockCode;
	this.baseDate = baseDate;
    }

    public String getStockCode() {
	return stockCode;
    }

    public void setStockCode(String stockCode) {
	this.stockCode = stockCode;
    }

    public LocalDate getBaseDate() {
	return baseDate;
    }

    public void setBaseDate(LocalDate baseDate) {
	this.baseDate = baseDate;
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

    public BigDecimal getTotalEps() {
	return totalEps;
    }

    public void setTotalEps(BigDecimal totalEps) {
	this.totalEps = totalEps;
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
