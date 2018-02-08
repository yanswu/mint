package idv.mint.batch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import idv.mint.batch.type.BatchStatusType;
import idv.mint.bean.Stock;
import idv.mint.bean.StockCategory;
import idv.mint.context.enums.SymbolType;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.support.PathSettings;
import idv.mint.util.FileUtils;
import idv.mint.util.crawl.Crawler;
import idv.mint.util.stock.StockCreator;

public class StockCsvWriterBatch extends AbstractInitBatch {

    @Override
    public BatchStatusType process() throws Exception {

	// 1. stock category
	stockCategoryCsvWriter(StockMarketType.TSE, PathSettings.STOCK_CATEGORY_TSE_CSV);
	stockCategoryCsvWriter(StockMarketType.OTC, PathSettings.STOCK_CATEGORY_OTC_CSV);

	// 2. stock
	stockCsvWriter(StockMarketType.TSE, PathSettings.STOCK_CATEGORY_TSE_CSV, PathSettings.STOCK_TSE_CSV);
	stockCsvWriter(StockMarketType.OTC, PathSettings.STOCK_CATEGORY_OTC_CSV, PathSettings.STOCK_OTC_CSV);

	// 3. stock EPS
	stockEPSCsvWriter(StockMarketType.TSE, PathSettings.STOCK_TSE_CSV, PathSettings.STOCK_EPS_TSE_CSV);
	stockEPSCsvWriter(StockMarketType.OTC, PathSettings.STOCK_OTC_CSV, PathSettings.STOCK_EPS_OTC_CSV);

	// 4. stock dividends;
	stockDividendCsvWriter(StockMarketType.TSE, PathSettings.STOCK_TSE_CSV, PathSettings.STOCK_DIVIDEND_TSE_CSV);
	stockDividendCsvWriter(StockMarketType.OTC, PathSettings.STOCK_OTC_CSV, PathSettings.STOCK_DIVIDEND_OTC_CSV);
	
	return BatchStatusType.SUCCESS;
    }

    public static void main(String[] args) throws Exception {

//	StockCsvWriterBatch stockCsvWriterBatch = new StockCsvWriterBatch();
//	stockCsvWriterBatch.execute();
//	stockCsvWriterBatch.getBatchProcessSeconds();
	
    }

    private static void stockCategoryCsvWriter(StockMarketType marketType, PathSettings inPathSettings) throws IOException {

	Path writePath = inPathSettings.getPath();

	// 1. parser HTML
	Crawler crawler = Crawler.createWebCrawler();
	List<String> lines = crawler.getStockCategory(marketType);

	// 2. clean file content
	FileUtils.cleanFile(writePath);

	// 3. write files
	FileUtils.writeFile(writePath, lines);
    }

    private void stockCsvWriter(StockMarketType marketType, PathSettings inPath, PathSettings outPath) throws Exception {

	Path readPath = inPath.getPath();
	Path writePath = outPath.getPath();

	List<String> categoryLines = Files.readAllLines(readPath, StandardCharsets.UTF_8);

	if (CollectionUtils.isNotEmpty(categoryLines)) {

	    Crawler crawler = Crawler.createWebCrawler();

	    List<StockCategory> stockCategoryList = StockCreator.createStockCategoryList(categoryLines);

	    FileUtils.cleanFile(writePath);

	    for (StockCategory stockCategory : stockCategoryList) {

		List<String> lines = crawler.getStock(marketType, stockCategory.getName());

		List<String> textLines = lines.stream().map(line -> {

		    // pattern : marketType,sequence,stockCategoryName,stockCode,stockName
		    StringBuilder sb = new StringBuilder();
		    sb.append(stockCategory.getMarketType().getValue());
		    sb.append(SymbolType.COMMA);
		    sb.append(stockCategory.getOrderNo());
		    sb.append(SymbolType.COMMA);
		    sb.append(stockCategory.getName());
		    sb.append(SymbolType.COMMA);
		    sb.append(line);
		    return sb.toString();

		}).collect(Collectors.toList());

		FileUtils.writeFileAppend(writePath, textLines);
	    }
	}
    }

    private void stockEPSCsvWriter(StockMarketType marketType, PathSettings inPathSettings, PathSettings outPathSettings) throws IOException {

	Path readPath = inPathSettings.getPath();
	Path writePath = outPathSettings.getPath();

	List<String> lines = Files.readAllLines(readPath, StandardCharsets.UTF_8);

	if (CollectionUtils.isNotEmpty(lines)) {

	    Crawler crawler = Crawler.createWebCrawler();
	    List<Stock> stockList = StockCreator.createStockList(lines);

	    FileUtils.cleanFile(writePath);

	    for (Stock stock : stockList) {
		// pattern : stockCode, pageStockCode, year, q1, q2, q3, q4
		FileUtils.writeFileAppend(writePath, crawler.getStockEPS(stock.getStockCode()));
	    }
	}
    }

    private void stockDividendCsvWriter(StockMarketType marketType, PathSettings inPathSettings, PathSettings outPathSettings) throws IOException {

	Path readPath = inPathSettings.getPath();
	Path writePath = outPathSettings.getPath();

	List<String> lines = Files.readAllLines(readPath, StandardCharsets.UTF_8);

	if (CollectionUtils.isNotEmpty(lines)) {

	    List<Stock> stockList = StockCreator.createStockList(lines);

	    FileUtils.cleanFile(writePath);

	    Crawler crawler = Crawler.createWebCrawler();

	    for (Stock stock : stockList) {
		// pattern : stockCode, rocYear, cashDividend, stockDividend
		FileUtils.writeFileAppend(writePath, crawler.getStockDividend(stock.getStockCode()));
	    }
	}
    }

}
