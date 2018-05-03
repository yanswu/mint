package idv.mint.batch.handler.persistence;

import java.util.List;

import idv.mint.batch.BatchContextUtils;
import idv.mint.batch.Context;
import idv.mint.batch.handler.TaskHandler;
import idv.mint.bean.StockSheet;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.service.CrawlerService;
import idv.mint.service.StockService;

public class StockSheetPersistHandler extends TaskHandler {

	@Override
	public boolean execute(Context<Context.Constants, Object> context) throws Exception {

		CrawlerService crawlerService = getSpringBean(context, CrawlerService.class);
		StockService stockService = getSpringBean(context, StockService.class);

		List<StockMarketType> marketTypeList = BatchContextUtils.getStockMarketType(context);

		for (StockMarketType marketType : marketTypeList) {

			List<StockSheet> stockSheetList = crawlerService.getStockSheetList(marketType);

			stockService.saveStockSheetEntities(stockSheetList);
		}

		return true;
	}

}
