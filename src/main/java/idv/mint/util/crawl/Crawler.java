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
     *  pattern : stockCode,stockName (2330, TSMC)
     * </pre>
     * @param marketType
     * @param categoryName
     * @return
     */
    public List<String> getStockLines(StockMarketType marketType,String categoryName);
    /**
     * <pre>
     *  marketType (市場分類)
     *  pattern : marketType(1,2), sequence, categoryName,stockCode,stockName
     * </pre>
     * @param marketType
     * @return
     * @throws IOException
     */
    public List<String> getStockLines(StockMarketType marketType)throws IOException;
    
    
    /**
     * <pre>
     * pattern : stockCode, pageStockCode, year, q1, q2, q3, q4
     * </pre>
     * 取得歷年度EPS
     * @param stockCode
     * @return
     * @throws IOException
     */
    public List<String> getStockEPSLines(String stockCode)  ;
    
    /**
     * <pre>
     * pattern : stockCode, rocYear, cashDividend, stockDividend
     * 2330,105,7.00,0.00
     * </pre>
     * 取得歷年度股利(股票股利 現金股利)
     * 
     * @param stockCode
     * @return
     * @throws IOException
     */
    public List<String> getStockDividendLines(String stockCode) ;
    
    public List<String> getStockRoeNetIncomeLines(String stockCode);
    
    /**
     * @param stockCode
     * @return
     */
    default public String getStockPrice(String stockCode) {
	return null;
    }


}
