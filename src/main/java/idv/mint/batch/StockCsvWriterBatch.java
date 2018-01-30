package idv.mint.batch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import idv.mint.bean.Stock;
import idv.mint.bean.StockCategory;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.support.PathSettings;
import idv.mint.util.FileUtils;
import idv.mint.util.crawl.Crawler;
import idv.mint.util.stock.StockCreator;

public class StockCsvWriterBatch {

    public static void main(String[] args) throws Exception {
	
	Date startDate = new Date();
	FastDateFormat dateFormat = DateFormatUtils.ISO_DATETIME_FORMAT;
	
	
//	task1();
//	task2();
//	task3();
//	task4();
	Date endDate = new Date();
	System.out.println("task start time "+ dateFormat.format(startDate));
	System.out.println("task end   time "+ dateFormat.format(endDate));
	System.out.println("it spends "+ getDiscrepantMinus(startDate, endDate)+" mins");
	

    }
    
    public static int getDiscrepantMinus(Date dateStart, Date dateEnd) {  
        return (int) ((dateEnd.getTime() - dateStart.getTime()) / (1000 * 60) );  
    }  

    public static void task1() throws IOException {

	// TSE Category
	stockCategoryWriterTask(StockMarketType.TSE);
	// OTC Category
	stockCategoryWriterTask(StockMarketType.OTC);
    }
    
    private static void stockCategoryWriterTask(StockMarketType marketType) throws IOException {

	Path writePath = null;

	if (marketType.isTSE()) {
	    writePath = PathSettings.STOCK_CATEGORY_TSE_CSV.getPath();
	} else if (marketType.isOTC()) {
	    writePath = PathSettings.STOCK_CATEGORY_OTC_CSV.getPath();
	}
	
	// 1. clean file content
	FileUtils.cleanFile(writePath);
	
	// 2. parser web html info
	Crawler crawler = Crawler.createWebCrawler();
	List<String> lines = crawler.getStockCategory(StockMarketType.TSE);

	// 3. write files
	FileUtils.writeFile(writePath, lines);
    }

    
    public static void task2() throws Exception {

	stockWriterTask(StockMarketType.TSE);

	stockWriterTask(StockMarketType.OTC);
    }


    private static void stockWriterTask(StockMarketType marketType) throws Exception {

	List<String> categoryLines = null;
	Path readPath = null;
	Path writePath = null;

	if (marketType.isTSE()) {

	    readPath = PathSettings.STOCK_CATEGORY_TSE_CSV.getPath();
	    writePath = PathSettings.STOCK_TSE_CSV.getPath();

	} else if (marketType.isOTC()) {

	    readPath = PathSettings.STOCK_CATEGORY_OTC_CSV.getPath();
	    writePath = PathSettings.STOCK_OTC_CSV.getPath();
	}

	categoryLines = Files.readAllLines(readPath, StandardCharsets.UTF_8);
	

	if (!CollectionUtils.isNotEmpty(categoryLines)) {

	    Crawler crawler = Crawler.createWebCrawler();
	    
	    FileUtils.cleanFile(writePath);
	    
	    List<StockCategory> stockCategoryList = StockCreator.createStockCategoryList(categoryLines);
	    
	    for (StockCategory stockCategory : stockCategoryList) {

		List<String> lines = crawler.getStock(marketType, stockCategory.getName());

		List<String> textLines = lines.stream().map(line -> {
		    
		    // pattern : marketType,sequence,stockCategoryName,stockCode,stockName
		    StringBuilder sb = new StringBuilder();
		    sb.append(stockCategory.getMarketType().getValue());
		    sb.append(",");
		    sb.append(stockCategory.getOrderNo());
		    sb.append(",");
		    sb.append(stockCategory.getName());
		    sb.append(",");
		    sb.append(line);
		    return sb.toString();

		}).collect(Collectors.toList());

		FileUtils.writeFileAppend(writePath, textLines);
	    }
	}
    }

    public static void task3() throws Exception {

	stockEPSWriterTask(StockMarketType.TSE);

	stockEPSWriterTask(StockMarketType.OTC);
    }

    private static void stockEPSWriterTask(StockMarketType marketType) throws IOException {

	List<String> lines = null;
	Path readPath = null;
	Path writePath = null;

	if (marketType.isTSE()) {
	    readPath = PathSettings.STOCK_TSE_CSV.getPath();
	    writePath = PathSettings.STOCK_EPS_TSE_CSV.getPath();

	} else if (marketType.isOTC()) {
	    readPath = PathSettings.STOCK_OTC_CSV.getPath();
	    writePath = PathSettings.STOCK_EPS_OTC_CSV.getPath();
	}
	
	lines = Files.readAllLines(readPath, StandardCharsets.UTF_8);
	FileUtils.cleanFile(writePath);

	if (!CollectionUtils.isNotEmpty(lines)) {
	    Crawler crawler = Crawler.createWebCrawler();
	    List<Stock> stockList = StockCreator.createStockList(lines);
	    for (Stock stock : stockList) {
		// pattern : stockCode, pageStockCode, year, q1, q2, q3, q4
		FileUtils.writeFileAppend(writePath, crawler.getStockEPS(stock.getStockCode()));
	    }
	}
    }
    
    public static void task4() throws Exception {

	stockDividendWriterTask(StockMarketType.TSE);

	stockDividendWriterTask(StockMarketType.OTC);
    }
    
    private static void stockDividendWriterTask(StockMarketType marketType) throws IOException {

	List<String> lines = null;
	Path readPath = null;
	Path writePath = null;

	if (marketType.isTSE()) {
	    readPath = PathSettings.STOCK_TSE_CSV.getPath();
	    writePath = PathSettings.STOCK_DIVIDEND_TSE_CSV.getPath();

	} else if (marketType.isOTC()) {
	    readPath = PathSettings.STOCK_OTC_CSV.getPath();
	    writePath = PathSettings.STOCK_DIVIDEND_OTC_CSV.getPath();
	}
	
	FileUtils.cleanFile(writePath);
	lines = Files.readAllLines(readPath, StandardCharsets.UTF_8);

	if (!CollectionUtils.isEmpty(lines)) {
	    Crawler crawler = Crawler.createWebCrawler();
	    List<Stock> stockList = StockCreator.createStockList(lines);
	    for (Stock stock : stockList) {
		// pattern : stockCode, rocYear, cashDividend, stockDividend
		FileUtils.writeFileAppend(writePath, crawler.getStockDividend(stock.getStockCode()));
	    }
	}
    }

}
