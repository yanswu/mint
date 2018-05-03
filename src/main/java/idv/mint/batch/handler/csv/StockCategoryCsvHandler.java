package idv.mint.batch.handler.csv;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import idv.mint.batch.BatchContextUtils;
import idv.mint.batch.Context;
import idv.mint.batch.handler.TaskHandler;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.support.PathSettings;
import idv.mint.util.FileUtils;
import idv.mint.util.crawl.Crawler;

public class StockCategoryCsvHandler extends TaskHandler {

    public StockCategoryCsvHandler() {
    }

    public StockCategoryCsvHandler(TaskHandler next) {
	super(next);
    }

    @Override
    public boolean execute(Context<Context.Constants, Object> context) throws Exception {
	
	Map<StockMarketType, PathSettings> map = new LinkedHashMap<>();
	map.put(StockMarketType.TSE, PathSettings.STOCK_CATEGORY_TSE_CSV);
	map.put(StockMarketType.OTC, PathSettings.STOCK_CATEGORY_OTC_CSV);
	
	List<StockMarketType> stockMarketType = BatchContextUtils.getStockMarketType(context);
	
	for (StockMarketType marketType : stockMarketType) {
	    
	    PathSettings writePath = map.get(marketType);
	    
	    stockCategoryCsvWriter(marketType, writePath);
	}

	return true;
    }
    

    private void stockCategoryCsvWriter(StockMarketType marketType, PathSettings inPathSettings) throws IOException {

	Path writePath = inPathSettings.getPath();

	// 1. parser HTML
	Crawler crawler = Crawler.createWebCrawler();

	List<String> lines = crawler.getStockCategoryLines(marketType);

	// 2. clean file content
	FileUtils.cleanFile(writePath);

	// 3. write files
	FileUtils.writeFile(writePath, lines);
    }

}
