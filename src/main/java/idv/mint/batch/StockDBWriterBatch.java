package idv.mint.batch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import idv.mint.bean.Stock;
import idv.mint.bean.StockCategory;
import idv.mint.bean.StockSheet;
import idv.mint.context.config.AppConfig;
import idv.mint.context.config.JpaConfig;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.service.CrawlerService;
import idv.mint.service.StockCategoryService;
import idv.mint.service.StockService;
import idv.mint.support.PathSettings;
import idv.mint.type.CrawlType;
import idv.mint.util.stock.StockCreator;

public class StockEntityWriterBatch {

    public static void main(String[] args) {

	AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
	context.register(AppConfig.class, JpaConfig.class);
	context.refresh();

	try {
	    // 1.
//	     saveStockCategoryTask(context);
	    // 2.
	     saveStockTask(context);
	    // 3.
	    saveStockSheet(context);
	} catch (IOException e) {
	    e.printStackTrace();
	}

	context.close();

    }

    private static void saveStockSheet(AnnotationConfigApplicationContext context ,StockMarketType... marketType) throws IOException {

	
	for (StockMarketType stockMarketType : marketType) {
	    
	    Path epsPath = null;
	    Path dividendPath = null;
	    
	    if(stockMarketType.isTSE()) {
		epsPath = PathSettings.STOCK_EPS_TSE_CSV.getPath();
		dividendPath = PathSettings.STOCK_DIVIDEND_TSE_CSV.getPath();
	    }else if(stockMarketType.isOTC()) {
		epsPath = PathSettings.STOCK_EPS_OTC_CSV.getPath();
		dividendPath = PathSettings.STOCK_DIVIDEND_OTC_CSV.getPath();
	    }
	    
	    List<String> epsLines = null;
	    List<String> dividendLines = null;
	    
	    if(epsPath != null) {
		epsLines = Files.readAllLines(epsPath);
	    }
	    
	    if(dividendPath != null) {
		dividendLines = Files.readAllLines(dividendPath);
	    }
	    
	    if(CollectionUtils.isNotEmpty(epsLines) && CollectionUtils.isNotEmpty(dividendLines)) {
		List<StockSheet> stockSheet = StockCreator.createStockSheetEpsList(epsLines, dividendLines);
		
	    }
	}
    }

    private static void saveStockTask(AnnotationConfigApplicationContext context) throws IOException {

	StockService stockService = context.getBean(StockService.class);

	List<StockMarketType> marketTypes = Arrays.asList(StockMarketType.TSE, StockMarketType.OTC);
	
	for (StockMarketType stockMarketType : marketTypes) {
	    
	    Path path = stockMarketType.isTSE() ? PathSettings.STOCK_TSE_CSV.getPath() : PathSettings.STOCK_OTC_CSV.getPath();
	    List<String> lines = Files.readAllLines(path);
	    List<Stock> stockList = StockCreator.createStockList(lines);
	    
	    stockService.saveStockEntities(stockList);
	}

    }

    public static void saveStockCategoryTask(AnnotationConfigApplicationContext context) throws IOException {

	CrawlerService crawlerService = context.getBean(CrawlerService.class);
	StockCategoryService stockCategoryService = context.getBean(StockCategoryService.class);

	List<StockCategory> stockCategories = crawlerService.getAllStockCategories(CrawlType.FILE);
	stockCategoryService.saveStockCategoryEntities(stockCategories);

    }
}
