package idv.mint.batch.handler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import idv.mint.batch.TaskHandler;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.support.PathSettings;
import idv.mint.util.FileUtils;
import idv.mint.util.crawl.Crawler;

public class StockCategoryCsvHandler extends TaskHandler {
    
    
    public StockCategoryCsvHandler() {}
    
    public StockCategoryCsvHandler(TaskHandler next) {
	super(next);
    }

    @Override
    public boolean execute(Map<String, Object> params) throws Exception{
	
	try {

	    stockCategoryCsvWriter(StockMarketType.TSE, PathSettings.STOCK_CATEGORY_TSE_CSV);
	    
	    stockCategoryCsvWriter(StockMarketType.OTC, PathSettings.STOCK_CATEGORY_OTC_CSV);
	    
	} catch (IOException e) {
	    
	    logger.error(e.getMessage());
	    
	    return false;
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
