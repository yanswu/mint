package idv.mint.batch.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import idv.mint.batch.TaskHandler;
import idv.mint.bean.StockCategory;
import idv.mint.context.enums.SymbolType;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.support.PathSettings;
import idv.mint.util.FileUtils;
import idv.mint.util.crawl.Crawler;
import idv.mint.util.stock.StockConverter;

public class StockCsvHandler extends TaskHandler {

    public StockCsvHandler() {
    };

    public StockCsvHandler(TaskHandler next) {
	super(next);
    };

    @Override
    public boolean execute(Map<String, Object> params) throws Exception {

	try {

	    stockCsvWriter(PathSettings.STOCK_CATEGORY_TSE_CSV, PathSettings.STOCK_TSE_CSV);

	    stockCsvWriter(PathSettings.STOCK_CATEGORY_OTC_CSV, PathSettings.STOCK_OTC_CSV);

	    return true;

	} catch (Exception e) {

	    logger.error(e.getMessage());
	}

	return false;
    }

    private void stockCsvWriter(PathSettings categoryPath, PathSettings stockOutPath) throws IOException {

	List<String> categoryLines = Files.readAllLines(categoryPath.getPath(), StandardCharsets.UTF_8);

	List<StockCategory> stockCategoryList = StockConverter.convertStockCategoryList(categoryLines);

	StockMarketType marketType = stockCategoryList.get(0).getMarketType();

	String comma = SymbolType.COMMA.getValue();

	Crawler crawler = Crawler.createWebCrawler();

	Path outPath = stockOutPath.getPath();

	FileUtils.cleanFile(outPath);

	for (StockCategory stockCategory : stockCategoryList) {

	    List<String> lines = crawler.getStockLines(marketType, stockCategory.getName());

	    List<String> textLines = lines.stream().map(line -> {

		// pattern : marketType,sequence,stockCategoryName,stockCode,stockName
		StringBuilder sb = new StringBuilder();

		sb.append(stockCategory.getMarketType().getValue());
		sb.append(comma);
		sb.append(stockCategory.getOrderNo());
		sb.append(comma);
		sb.append(stockCategory.getName());
		sb.append(comma);
		sb.append(line);
		return sb.toString();

	    }).collect(Collectors.toList());

	    FileUtils.writeFileAppend(outPath, textLines);

	}
    }
}
