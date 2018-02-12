package idv.mint.batch;

import java.util.Arrays;
import java.util.List;

import idv.mint.batch.type.BatchStatusType;
import idv.mint.bean.Stock;
import idv.mint.bean.StockCategory;
import idv.mint.bean.StockSheet;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.service.CrawlerService;
import idv.mint.service.StockCategoryService;
import idv.mint.service.StockService;
import idv.mint.service.StockSheetService;
import idv.mint.type.CrawlType;

public class StockDBWriterBatch extends AbstractRootBatch {

    @Override
    public BatchStatusType process() throws Exception {

	CrawlerService crawlerService = this.getSpringBean(CrawlerService.class);
	StockCategoryService stockCategoryService = this.getSpringBean(StockCategoryService.class);
	StockService stockService = this.getSpringBean(StockService.class);
	StockSheetService stockSheetService = this.getSpringBean(StockSheetService.class);

	CrawlType fileCrawlType = CrawlType.FILE;
	List<StockMarketType> marketTypes = Arrays.asList(StockMarketType.TSE, StockMarketType.OTC);

	// 1. insert stockCategory
//	for (StockMarketType stockMarketType : marketTypes) {
//	    List<StockCategory> stockCategoryList = crawlerService.getStockCategoryList(fileCrawlType, stockMarketType);
//	    stockCategoryService.saveStockCategoryEntities(stockCategoryList);
//	}

	// 2. insert stock
//	for (StockMarketType stockMarketType : marketTypes) {
//	    List<Stock> stockList = crawlerService.getStockList(fileCrawlType, stockMarketType);
//	    stockService.saveStockEntities(stockList);
//	}

	// 3.insert stockSheet
	for (StockMarketType stockMarketType : marketTypes) {
	    List<StockSheet> stockSheetList = crawlerService.getStockSheetList(fileCrawlType, stockMarketType);
	    stockSheetService.saveStockSheetEntities(stockSheetList);
	}

	return BatchStatusType.SUCCESS;
    }

    public static void main(String[] args) {

	new StockDBWriterBatch().execute();
    }

}
