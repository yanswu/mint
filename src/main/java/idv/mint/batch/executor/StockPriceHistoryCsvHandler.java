package idv.mint.batch.executor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import idv.mint.batch.TaskHandler;
import idv.mint.bean.Stock;
import idv.mint.support.PathSettings;
import idv.mint.util.FileUtils;
import idv.mint.util.crawl.Crawler;
import idv.mint.util.stock.StockConverter;

public class StockPriceHistoryCsvHandler extends TaskHandler {

    @Override
    public boolean execute(Map<String, Object> params) throws Exception {
	
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

	for (Stock stock : stockList) {
	    
	    List<String> lines = crawler.getStockPriceHistoryLines(stock.getStockCode());
	    
	    FileUtils.writeFileAppend(writePath, lines);
	}
    }
}
