package idv.mint.batch.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import idv.mint.batch.BatchSettings;
import idv.mint.batch.Context;
import idv.mint.batch.TaskHandler;
import idv.mint.bean.Stock;
import idv.mint.support.PathSettings;
import idv.mint.util.FileUtils;
import idv.mint.util.crawl.Crawler;
import idv.mint.util.crawl.impl.WebCrawler;
import idv.mint.util.stock.StockConverter;

public class StockPriceHistoryCsvHandler extends TaskHandler {

    @Override
    public boolean execute(Context<BatchSettings, Object> context) throws Exception {
	
	priceHistoryCsvWriter(PathSettings.STOCK_TSE_CSV, PathSettings.STOCK_PRICE_HISTORY_TSE_CSV);

	priceHistoryCsvWriter(PathSettings.STOCK_OTC_CSV, PathSettings.STOCK_PRICE_HISTORY_OTC_CSV);
	
	return true;
    }

    private void priceHistoryCsvWriter(PathSettings stockInPath, PathSettings stockOutPath) throws IOException {

	List<String> stockLines = Files.readAllLines(stockInPath.getPath(), StandardCharsets.UTF_8);

	List<Stock> stockList = StockConverter.convertStockList(stockLines);

	Crawler crawler = Crawler.createWebCrawler();

	Path writePath = stockOutPath.getPath();

	FileUtils.cleanFile(writePath);
	
	boolean isExecute = true;
	int count = 0;
	
	for (Stock stock : stockList) {
	    
	    String stockCode = stock.getStockCode();
	    
//	    if(StringUtils.equals(stockCode, "3207")) {
//		isExecute = true;
//	    }
	    
	    if(isExecute) {
		
		List<String> lines = crawler.getStockPriceHistoryLines(stockCode);
		
		if(CollectionUtils.isEmpty(lines)) {
//		    if(count == 3) {
//			System.exit(0);
//		    }
//		    count ++;
		    WebCrawler.writeErrorLogFile(stockCode);
		    
		}else {
		    FileUtils.writeFileAppend(writePath, lines);
		}
		
	    }
	    
	}
    }
}
