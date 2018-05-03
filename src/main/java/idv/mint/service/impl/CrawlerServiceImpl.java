package idv.mint.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import idv.mint.util.stock.StockConverter;

@Service("crawlerService")
public class CrawlerServiceImpl implements CrawlerService {

    private static final Logger logger = LogManager.getLogger(CrawlerServiceImpl.class);
    
    private final static String comma = SymbolType.COMMA.getValue();
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
	return StockConverter.convertStockCategoryList(lines);
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

	stockList = StockConverter.convertStockList(lines);

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

	return StockConverter.createStockSheetList(epsLines, dividendLines,null,null);
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
    
    /**
     * <pre>
     *   key 為 stockCode 
     * </pre>
     * @param readPath
     * @return
     * @throws IOException
     */
    private Map<String,List<String>> convertStockCodeMap(Path readPath) throws IOException{
    	
    	List<String> lines = Files.readAllLines(readPath);
    	Map<String, List<String>> map = lines.stream().collect(Collectors.groupingBy(line->{
    		return line.split(comma)[0];
    	}));
    	
    	return map;
    	
//    	Map<String,List<String>> map = new LinkedHashMap<>();
//    	
//		List<String> lines = Files.readAllLines(readPath);
//
//		for (String line : lines) {
//
//			String stockCode = StringUtils.split(line, SymbolType.COMMA.getValue())[0];
//
//			if (!map.containsKey(stockCode)) {
//				map.put(stockCode, new ArrayList<>());
//			}
//
//			map.get(stockCode).add(line);
//		}
//		return map;
    }
    
    @Override
    public List<StockSheet> getStockSheetList(StockMarketType marketType) throws IOException {

		Path epsPath = PathSettings.find(marketType, PathSettings.Types.EPS).getPath();
		Path dividendPath = PathSettings.find(marketType, PathSettings.Types.DIVIDEND).getPath();
		Path incomeStatementPath = PathSettings.find(marketType, PathSettings.Types.INCOMESTATEMENT).getPath();
		Path balanceSheetPath = PathSettings.find(marketType, PathSettings.Types.BALANCESHEET).getPath();
	
	    Map<String, List<String>> stockEpsMap = convertStockCodeMap(epsPath);
	    Map<String, List<String>> stockDividendMap = convertStockCodeMap(dividendPath);
	    Map<String, List<String>> stockIncomeStatementMap = convertStockCodeMap(incomeStatementPath);
	    Map<String, List<String>> stockBalanceSheetMap = convertStockCodeMap(balanceSheetPath);

	    
	    return stockEpsMap.entrySet().stream().flatMap(e->{
	    	String stockCode = e.getKey();
	    	List<String> epsLines = e.getValue();
	    	List<String> dividendLines = stockDividendMap.get(stockCode);
	    	List<String> incomeStatementLines = stockIncomeStatementMap.get(stockCode);
	    	List<String> balanceSheetLines = stockBalanceSheetMap.get(stockCode);
	    	return StockConverter.createStockSheetList(epsLines, dividendLines,incomeStatementLines,balanceSheetLines).stream();
	    }).collect(Collectors.toList());
	    
	    // filter

    }

    /**
     * 排除舊資料
     * 
     * @param stockSheetList
     * @return
     */
    private List<StockSheet> filterEmptyEPSStockSheet(List<StockSheet> stockSheetList) {

	return stockSheetList.stream().filter(e -> {
	    return e.getEpsQ1() != null;
	}).collect(Collectors.toList());
    }

    @Override
    public BigDecimal getStockPrice(String stockCode) {

	Crawler crawler = Crawler.createWebCrawler();
	String stockPrice = crawler.getStockPrice(stockCode);

	if (StringUtils.isNotBlank(stockPrice) && NumberUtils.isNumber(stockPrice)) {
	    return new BigDecimal(stockPrice);
	}
	return BigDecimal.ZERO;
    }

}
