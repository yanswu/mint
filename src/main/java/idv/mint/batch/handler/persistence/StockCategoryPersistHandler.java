package idv.mint.batch.handler.persistence;

import java.util.List;

import idv.mint.batch.BatchContextUtils;
import idv.mint.batch.Context;
import idv.mint.batch.handler.TaskHandler;
import idv.mint.bean.StockCategory;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.service.CrawlerService;
import idv.mint.service.StockService;
import idv.mint.type.CrawlType;

public class StockCategoryPersistHandler extends TaskHandler {

    @Override
    public boolean execute(Context<Context.Constants, Object> context) throws Exception {

	CrawlerService crawlerService = getSpringBean(context, CrawlerService.class);
	StockService stockService = getSpringBean(context, StockService.class);

	List<StockMarketType> marketTypeList = BatchContextUtils.getStockMarketType(context);
	
	for (StockMarketType marketType : marketTypeList) {
	    
	    List<StockCategory> stockCategoryList = crawlerService.getStockCategoryList(CrawlType.FILE, marketType);
	    
	    stockService.saveStockCategoryEntities(stockCategoryList);
	}
	
	return true;
    }

}
