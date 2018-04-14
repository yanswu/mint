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

public class StockDividendCsvHandler extends TaskHandler {
    
    
    public StockDividendCsvHandler(){};

    public StockDividendCsvHandler(TaskHandler next){
	super(next);
    };
    
    @Override
    public boolean execute(Map<String, Object> params) throws Exception {

	try {
	    
	    stockDividendCsvWriter(PathSettings.STOCK_TSE_CSV, PathSettings.STOCK_DIVIDEND_TSE_CSV);
	    
	    stockDividendCsvWriter(PathSettings.STOCK_OTC_CSV, PathSettings.STOCK_DIVIDEND_OTC_CSV);

	} catch (Exception e) {
	    logger.error(e.getMessage());
	    return false;
	}
	return true;
    }

    private void stockDividendCsvWriter(PathSettings inPathSettings, PathSettings outPathSettings) throws IOException {

	List<String> lines = Files.readAllLines(inPathSettings.getPath(), StandardCharsets.UTF_8);

	List<Stock> stockList = StockConverter.convertStockList(lines);

	Path writePath = outPathSettings.getPath();

	FileUtils.cleanFile(writePath);

	Crawler crawler = Crawler.createWebCrawler();

	for (Stock stock : stockList) {
	    // pattern : stockCode, rocYear, cashDividend, stockDividend
	    List<String> dividendLines = crawler.getStockDividendLines(stock.getStockCode());
	    FileUtils.writeFileAppend(writePath, dividendLines);
	}
    }
}
