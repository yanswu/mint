package idv.mint.batch.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import idv.mint.batch.BatchSettings;
import idv.mint.batch.Context;
import idv.mint.batch.TaskHandler;
import idv.mint.bean.Stock;
import idv.mint.support.PathSettings;
import idv.mint.util.FileUtils;
import idv.mint.util.crawl.Crawler;
import idv.mint.util.stock.StockConverter;

public class StockIncomeStatementCsvHandler extends TaskHandler {

    @Override
    public boolean execute(Context<BatchSettings, Object> context) throws Exception {

	// TSE
	incomeStatementCsvWriter(PathSettings.STOCK_TSE_CSV, PathSettings.STOCK_INCOMESTATEMENT_TSE_CSV);

	incomeStatementCsvWriter(PathSettings.STOCK_OTC_CSV, PathSettings.STOCK_INCOMESTATEMENT_OTC_CSV);

	return true;
    }

    private void incomeStatementCsvWriter(PathSettings stockInPath, PathSettings stockOutPath) throws IOException {

	List<String> lines = Files.readAllLines(stockInPath.getPath(), StandardCharsets.UTF_8);

	List<Stock> stockList = StockConverter.convertStockList(lines);

	Crawler crawler = Crawler.createWebCrawler();

	Path writePath = stockOutPath.getPath();

	FileUtils.cleanFile(writePath);

	for (Stock stock : stockList) {
	    // pattern : stockCode, rocYear,netIncome
	    List<String> incomeStatementLines = crawler.getIncomeStatementLines(stock.getStockCode());
	    FileUtils.writeFileAppend(writePath, incomeStatementLines);
	}
    }

}
