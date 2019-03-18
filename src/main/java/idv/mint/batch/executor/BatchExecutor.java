package idv.mint.batch.executor;

import idv.mint.batch.BatchContextUtils;
import idv.mint.batch.Context;
import idv.mint.batch.handler.csv.StockBalanceSheetCsvHandler;
import idv.mint.batch.handler.csv.StockCategoryCsvHandler;
import idv.mint.batch.handler.csv.StockCsvHandler;
import idv.mint.batch.handler.csv.StockDividendCsvHandler;
import idv.mint.batch.handler.csv.StockEpsCsvHandler;
import idv.mint.batch.handler.csv.StockIncomeStatementCsvHandler;
import idv.mint.batch.handler.csv.StockPriceHistoryCsvHandler;
import idv.mint.batch.handler.persistence.StockCategoryPersistHandler;
import idv.mint.batch.handler.persistence.StockPersistHandler;
import idv.mint.batch.handler.persistence.StockSheetPersistHandler;

public class BatchExecutor {
    
    public static void main(String[] args) throws Exception {
	
//	Context<BatchSettings, Object> context = ContextParamUtils.createContext();
	
//	updateStockAndCategory(context);
//	
//	updateSheet(context);
//
//	updateIncomeStatement(context);
//	
//	updateBalanceSheet(context);
//
//	updateStockPriceHistory(context);
	
	initDBData();
    }
    
    private static void updateStockAndCategory(Context<Context.Constants, Object> context) {
	
	StockCategoryCsvHandler categoryCsvHandler = new StockCategoryCsvHandler();
	
	StockCsvHandler stockCsvHandler = new StockCsvHandler();
	
	categoryCsvHandler.setNextHandler(stockCsvHandler);
	
	categoryCsvHandler.executeTask(context);

    }

    private static void updateSheet(Context<Context.Constants, Object> context) {
	
	StockEpsCsvHandler epsCsvHandler = new StockEpsCsvHandler();
	
	StockDividendCsvHandler dividendCsvHandler = new StockDividendCsvHandler();
	
	epsCsvHandler.setNextHandler(dividendCsvHandler);
	
	epsCsvHandler.executeTask(context);
	
    }

    private static void updateIncomeStatement(Context<Context.Constants, Object> context) {
	
	StockIncomeStatementCsvHandler incomeStatementCsvHandler = new StockIncomeStatementCsvHandler();
	
	incomeStatementCsvHandler.executeTask(context);
	
    }

    private static void updateBalanceSheet(Context<Context.Constants, Object> context) {
	
	StockBalanceSheetCsvHandler balanceSheetCsvHandler = new StockBalanceSheetCsvHandler();
	
	balanceSheetCsvHandler.executeTask(context);
	
    }

    private static void updateStockPriceHistory(Context<Context.Constants, Object> context) {
	
	StockPriceHistoryCsvHandler stockPriceCsvHandler = new StockPriceHistoryCsvHandler();
	
	stockPriceCsvHandler.executeTask(context);
	
    }

    private static void initDBData() {
	
	Context<Context.Constants, Object> context = BatchContextUtils.createContextWithSpringConfig();
	
	StockCategoryPersistHandler stockCatetoryPersistHandler = new StockCategoryPersistHandler();
	StockPersistHandler stockPersistHandler = new StockPersistHandler();
	StockSheetPersistHandler stockSheetPersistHandler = new StockSheetPersistHandler();	
	
	stockCatetoryPersistHandler.setNextHandler(stockPersistHandler);
	stockPersistHandler.setNextHandler(stockSheetPersistHandler);
	
//	stockCatetoryPersistHandler.executeTask(context);
	stockSheetPersistHandler.executeTask(context);
    }
    
    

}
