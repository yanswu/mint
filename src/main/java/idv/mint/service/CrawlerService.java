package idv.mint.service;

import java.io.IOException;
import java.util.List;

import idv.mint.bean.StockCategory;
import idv.mint.bean.StockSheet;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.type.CrawlType;

public interface CrawlerService {

    /**
     * <pre>
     * 取得 市場 (TSE or OTC)目錄
     * </pre>
     * 
     * @param marketType
     * @return
     * @throws IOException
     */
    public List<StockCategory> getStockCategory(CrawlType type, StockMarketType marketType) throws IOException;

    /**
     * <pre>
     *    取得 TSE OTC  市場 目錄
     * </pre>
     * @return
     * @throws IOException
     */
    public List<StockCategory> getAllStockCategories(CrawlType type) throws IOException;

    /**
     * 
     * @param stockCode
     * @return
     * @throws IOException
     */
    public List<StockSheet> getStockSheetList(CrawlType type, String stockCode) throws IOException;

}
