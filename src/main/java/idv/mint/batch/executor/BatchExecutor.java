package idv.mint.batch.executor;

import idv.mint.batch.BatchSettings;
import idv.mint.batch.Context;
import idv.mint.batch.ContextParamUtils;
import idv.mint.batch.handler.StockBalanceSheetCsvHandler;
import idv.mint.batch.handler.StockCategoryCsvHandler;
import idv.mint.batch.handler.StockCategoryPersistHandler;
import idv.mint.batch.handler.StockCsvHandler;
import idv.mint.batch.handler.StockDividendCsvHandler;
import idv.mint.batch.handler.StockEpsCsvHandler;
import idv.mint.batch.handler.StockIncomeStatementCsvHandler;
import idv.mint.batch.handler.StockPersistHandler;
import idv.mint.batch.handler.StockPriceHistoryCsvHandler;

public class BatchExecutor {
    
    public static void main(String[] args) throws Exception {
	
	
	
	Context<BatchSettings, Object> context = ContextParamUtils.createContext();
	
	
	updateStockAndCategory(context);
	
	updateSheet(context);

	updateIncomeStatement(context);
	
	updateBalanceSheet(context);

	updateStockPriceHistory(context);
	
	
    }
    
    private static void updateStockAndCategory(Context<BatchSettings, Object> context) {
	
	StockCategoryCsvHandler categoryCsvHandler = new StockCategoryCsvHandler();
	
	StockCsvHandler stockCsvHandler = new StockCsvHandler();
	
	categoryCsvHandler.setNextHandler(stockCsvHandler);
	
	categoryCsvHandler.executeTask(context);

    }

    private static void updateSheet(Context<BatchSettings, Object> context) {
	
	StockEpsCsvHandler epsCsvHandler = new StockEpsCsvHandler();
	
	StockDividendCsvHandler dividendCsvHandler = new StockDividendCsvHandler();
	
	epsCsvHandler.setNextHandler(dividendCsvHandler);
	
	epsCsvHandler.executeTask(context);
	
    }

    private static void updateIncomeStatement(Context<BatchSettings, Object> context) {
	
	StockIncomeStatementCsvHandler incomeStatementCsvHandler = new StockIncomeStatementCsvHandler();
	
	incomeStatementCsvHandler.executeTask(context);
	
    }

    private static void updateBalanceSheet(Context<BatchSettings, Object> context) {
	
	StockBalanceSheetCsvHandler balanceSheetCsvHandler = new StockBalanceSheetCsvHandler();
	
	balanceSheetCsvHandler.executeTask(context);
	
    }

    private static void updateStockPriceHistory(Context<BatchSettings, Object> context) {
	
	StockPriceHistoryCsvHandler stockPriceCsvHandler = new StockPriceHistoryCsvHandler();
	
	stockPriceCsvHandler.executeTask(context);
	
    }

    private static void initDBData() {
	
	Context<BatchSettings, Object> context = ContextParamUtils.createContextWithSpringConfig();
	
	StockCategoryPersistHandler stockCatetoryHandler = new StockCategoryPersistHandler();
	
	StockPersistHandler stockHandler = new StockPersistHandler();
	
	stockCatetoryHandler.setNextHandler(stockHandler);
	
	
	stockCatetoryHandler.executeTask(context);
	
    }
    
    

}
