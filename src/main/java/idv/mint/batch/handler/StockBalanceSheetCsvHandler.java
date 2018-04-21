package idv.mint.batch.handler;

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

public class StockBalanceSheetCsvHandler extends TaskHandler {

    @Override
    public boolean execute(Map<String, Object> params) throws Exception {

	balanceSheetCsvWriter(PathSettings.STOCK_TSE_CSV, PathSettings.STOCK_BALANCESHEET_TSE_CSV);

	balanceSheetCsvWriter(PathSettings.STOCK_OTC_CSV, PathSettings.STOCK_BALANCESHEET_OTC_CSV);

	return true;
    }

    private void balanceSheetCsvWriter(PathSettings stockInPath, PathSettings stockOutPath) throws IOException {

	List<String> lines = Files.readAllLines(stockInPath.getPath(), StandardCharsets.UTF_8);

	List<Stock> stockList = StockConverter.convertStockList(lines);

	Crawler crawler = Crawler.createWebCrawler();

	Path writePath = stockOutPath.getPath();

	FileUtils.cleanFile(writePath);

	for (Stock stock : stockList) {
	    
	    List<String> balanceSheetLines = crawler.getBalanceSheetLines(stock.getStockCode());
	    
	    FileUtils.writeFileAppend(writePath, balanceSheetLines);
	}
    }

}
