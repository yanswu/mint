package idv.mint.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import idv.mint.bean.StockCategory;
import idv.mint.bean.StockSheet;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.service.CrawlerService;
import idv.mint.type.CrawlType;
import idv.mint.util.crawl.Crawler;
import idv.mint.util.stock.StockCreator;

@Service("crawlerService")
public class CrawlerServiceImpl implements CrawlerService {

    private static final Logger logger = LogManager.getLogger(CrawlerServiceImpl.class);

    /**
     * 
     * 
     * @param marketType
     * @return
     * @throws IOException
     */
    @Override
    public List<StockCategory> getStockCategory(CrawlType crawlType, StockMarketType marketType) throws IOException {

	Crawler crawler = getCrawler(crawlType);
	List<String> lines = crawler.getStockCategory(marketType);
	return StockCreator.createStockCategoryList(lines);
    }

    /**
     * 
     * @return
     * @throws IOException
     */
    @Override
    public List<StockCategory> getAllStockCategories(CrawlType crawlType) throws IOException {

	return Arrays.stream(StockMarketType.values()).flatMap(marketType -> {
	    try {
		return this.getStockCategory(crawlType, marketType).stream();
	    } catch (IOException e) {
		logger.error(e, e);
		throw new RuntimeException(e);
	    }
	}).collect(Collectors.toList());
    }

    /**
     * 
     * @param stockCode
     * @return
     * @throws IOException
     */
    @Override
    public List<StockSheet> getStockSheetList(CrawlType crawlType, String stockCode) throws IOException {

	Crawler crawler = getCrawler(crawlType);
	List<String> epsLines = crawler.getStockEPS(stockCode);
	List<String> dividendLines = crawler.getStockDividend(stockCode);

	return StockCreator.createStockSheetEpsList(epsLines, dividendLines);
    }

    /**
     * 取得 File or Web Crawler
     * 
     * @param crawlType
     * @return
     */
    private Crawler getCrawler(CrawlType crawlType) {

	if (crawlType.isOnLine()) {
	    return Crawler.createWebCrawler();
	} else if (crawlType.isFile()) {
	    return Crawler.createFileCrawler();
	}
	return Crawler.createNonOpCrawler();
    }
}
