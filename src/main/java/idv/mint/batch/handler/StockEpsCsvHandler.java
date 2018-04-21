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

public class StockEpsCsvHandler extends TaskHandler {

    public StockEpsCsvHandler() {
    }

    public StockEpsCsvHandler(TaskHandler next) {
	super(next);
    }

    @Override
    public boolean execute(Map<String, Object> params) throws Exception {

	try {

	    // TSE
	    stockEPSCsvWriter(PathSettings.STOCK_TSE_CSV, PathSettings.STOCK_EPS_TSE_CSV);
	    // OTC
	    stockEPSCsvWriter(PathSettings.STOCK_OTC_CSV, PathSettings.STOCK_EPS_OTC_CSV);

	} catch (Exception e) {

	    logger.error(e.getMessage());

	    return false;
	}

	return true;
    }

    private void stockEPSCsvWriter(PathSettings stockInPath, PathSettings stockEpsOutPath) throws IOException {

	List<String> lines = Files.readAllLines(stockInPath.getPath(), StandardCharsets.UTF_8);

	List<Stock> stockList = StockConverter.convertStockList(lines);

	Crawler crawler = Crawler.createWebCrawler();

	Path writePath = stockEpsOutPath.getPath();

	FileUtils.cleanFile(writePath);

	for (Stock stock : stockList) {
	    // pattern : stockCode, pageStockCode, year, q1, q2, q3, q4
	    List<String> epsLines = crawler.getStockEPSLines(stock.getStockCode());
	    FileUtils.writeFileAppend(writePath, epsLines);
	}
    }
}
