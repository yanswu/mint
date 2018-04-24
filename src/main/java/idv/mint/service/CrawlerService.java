package idv.mint.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import idv.mint.bean.Stock;
import idv.mint.bean.StockCategory;
import idv.mint.bean.StockSheet;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.type.CrawlType;

public interface CrawlerService {

    /**
     * <pre>
     * 取得 市場 (TSE or OTC)
     * </pre>
     * 
     * @param marketType
     * @return
     * @throws IOException
     */
    public List<StockCategory> getStockCategoryList(CrawlType type, StockMarketType marketType) throws IOException;

    /**
     * <pre>
     *    取得 TSE OTC  市場
     * </pre>
     * 
     * @param crawlType
     * @return
     * @throws IOException
     */
    public List<StockCategory> getAllStockCategories(CrawlType crawlType) throws IOException;

    /**
     * <pre>
     * 
     * </pre>
     * 
     * @param crawlType
     * @param marketType
     * @return
     * @throws IOException
     */
    public List<Stock> getStockList(CrawlType crawlType, StockMarketType marketType) throws IOException;

    /**
     * 
     * @param stockCode
     * @return
     * @throws IOException
     */
    public List<StockSheet> getStockSheetList(CrawlType type, String stockCode) throws IOException;

    /**
     * 
     * @param marketType
     * @return
     * @throws IOException
     */
    public List<StockSheet> getStockSheetList(StockMarketType marketType) throws IOException;

    /**
     * 取得目前股價
     * 
     * @param stockCode
     * @return
     */
    public BigDecimal getStockPrice(String stockCode);

}
