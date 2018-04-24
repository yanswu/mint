package idv.mint.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import idv.mint.entity.pk.StockPriceHistoryPk;

@Entity
@Table(name = "T_STOCK_SHEET")
public class StockPriceHistoryEntity implements Serializable {
    
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private StockPriceHistoryPk priceHistoryPk;
    
    @Column(name = "HIGH_PRICE")
    private BigDecimal highPrice;

    @Column(name = "LOW_PRICE")
    private BigDecimal lowPrice;
    
    
    
    
}
