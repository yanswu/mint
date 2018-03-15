package idv.mint.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import idv.mint.bean.Stock;
import idv.mint.bean.StockCategory;
import idv.mint.bean.StockSheet;
import idv.mint.context.enums.SymbolType;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.service.CrawlerService;
import idv.mint.support.PathSettings;
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
    public List<StockCategory> getStockCategoryList(CrawlType crawlType, StockMarketType marketType) throws IOException {

	Crawler crawler = getCrawler(crawlType);
	List<String> lines = crawler.getStockCategoryLines(marketType);
	return StockCreator.createStockCategoryList(lines);
    }

    /**
     * 
     * @return
     * @throws IOException
     */
    @Override
    public List<StockCategory> getAllStockCategories(CrawlType crawlType) throws IOException {

	return Arrays.asList(StockMarketType.TSE, StockMarketType.OTC).stream().flatMap(marketType -> {
	    try {
		return this.getStockCategoryList(crawlType, marketType).stream();
	    } catch (IOException e) {
		logger.error(e, e);
		throw new RuntimeException(e);
	    }
	}).collect(Collectors.toList());
    }

    @Override
    public List<Stock> getStockList(CrawlType crawlType, StockMarketType marketType) throws IOException {

	List<Stock> stockList = new ArrayList<>();

	Crawler crawler = getCrawler(crawlType);

	List<String> lines = crawler.getStockLines(marketType);
	
	stockList = StockCreator.createStockList(lines);

	return stockList;
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
	
	List<String> epsLines = crawler.getStockEPSLines(stockCode);
	
	List<String> dividendLines = crawler.getStockDividendLines(stockCode);

	return StockCreator.createStockSheetList(epsLines, dividendLines);
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

    @Override
    public List<StockSheet> getStockSheetList(CrawlType type, StockMarketType marketType) throws IOException {

	Path epsPath = null;
	Path dividendPath = null;

	if (marketType.isTSE()) {
	    epsPath = PathSettings.STOCK_EPS_TSE_CSV.getPath();
	    dividendPath = PathSettings.STOCK_DIVIDEND_TSE_CSV.getPath();
	} else if (marketType.isOTC()) {
	    epsPath = PathSettings.STOCK_EPS_OTC_CSV.getPath();
	    dividendPath = PathSettings.STOCK_DIVIDEND_OTC_CSV.getPath();
	}

	if (epsPath != null && dividendPath != null) {

	    Map<String, List<String>> stockEpsMap = new LinkedHashMap<>();
	    Map<String, List<String>> stockDividendMap = new LinkedHashMap<>();

	    List<String> epsLines = Files.readAllLines(epsPath);
	    
	    for (String line : epsLines) {
		
		String stockCode = StringUtils.split(line,SymbolType.COMMA.getValue())[0];

		if (!stockEpsMap.containsKey(stockCode)) {
		    stockEpsMap.put(stockCode, new ArrayList<>());
		}
		stockEpsMap.get(stockCode).add(line);
	    }


	    List<String> dividendLines = Files.readAllLines(dividendPath);

	    for (String line : dividendLines) {

		String stockCode = StringUtils.split(line,SymbolType.COMMA.getValue())[0];

		if (!stockDividendMap.containsKey(stockCode)) {
		    stockDividendMap.put(stockCode, new ArrayList<>());
		}
		stockDividendMap.get(stockCode).add(line);
	    }

	    List<StockSheet> stockSheetList = new ArrayList<>();

	    for (String stockCode : stockEpsMap.keySet()) {
		List<String> epsLineList = stockEpsMap.get(stockCode);
		List<String> dividendLineList = stockDividendMap.get(stockCode);
		
		stockSheetList.addAll(StockCreator.createStockSheetList(epsLineList, dividendLineList));
	    }
	    return stockSheetList;
	}

	return new ArrayList<>();
    }

}
