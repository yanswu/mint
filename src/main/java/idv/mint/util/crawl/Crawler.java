package idv.mint.util.crawl;

import java.io.IOException;
import java.util.List;

import idv.mint.entity.enums.StockMarketType;
import idv.mint.util.crawl.impl.FileCrawler;
import idv.mint.util.crawl.impl.NonOpCrawler;
import idv.mint.util.crawl.impl.WebCrawler;

public interface Crawler {
    	
    public static Crawler createWebCrawler() {
	return new WebCrawler();
    }

    public static Crawler createFileCrawler() {
	return new FileCrawler();
    }

    public static Crawler createNonOpCrawler() {
	return new NonOpCrawler();
    }
    
    /**
     * <pre>
     * 	 pattern :marketType(1,2), sequence, categoryName
     * </pre>
     * 取得上櫃或上市的分類名稱
     * @param marketType
     * @return
     * @throws IOException
     */
    public List<String> getStockCategoryLines(StockMarketType marketType) throws IOException;
    
    
    /**
     * <pre>
     *  marketType,categoryName (市場分類, 分類名稱)
     * </pre>
     * @param marketType
     * @param categoryName
     * @return
     * @throws IOException
     */
    public List<String> getStockLines(StockMarketType marketType,String categoryName)throws IOException;
    /**
     * <pre>
     *  marketType (市場分類)
     * </pre>
     * @param marketType
     * @return
     * @throws IOException
     */
    public List<String> getStockLines(StockMarketType marketType)throws IOException;
    
    
    /**
     * 取得歷年度EPS
     * @param stockCode
     * @return
     * @throws IOException
     */
    public List<String> getStockEPSLines(String stockCode) throws IOException ;
    
    /**
     * 取得歷年度股利(股票股利 現金股利)
     * 
     * @param stockCode
     * @return
     * @throws IOException
     */
    public List<String> getStockDividendLines(String stockCode) throws IOException ;
}
