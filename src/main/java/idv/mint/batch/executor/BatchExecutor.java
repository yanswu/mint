package idv.mint.batch.executor;

import java.util.HashMap;
import java.util.Map;

import idv.mint.batch.handler.StockBalanceSheetCsvHandler;
import idv.mint.batch.handler.StockCategoryCsvHandler;
import idv.mint.batch.handler.StockCsvHandler;
import idv.mint.batch.handler.StockDividendCsvHandler;
import idv.mint.batch.handler.StockEpsCsvHandler;
import idv.mint.batch.handler.StockIncomeStatementCsvHandler;
import idv.mint.batch.handler.StockPriceHistoryCsvHandler;

public class BatchExecutor {
    
    public static void main(String[] args) throws Exception {
	
	Map<String, Object> params = new HashMap<>();
	
	// 
//	updateStockAndCategory(params);
	
//	updateSheet(params);

//	updateIncomeStatement(params);
	
//	updateBalanceSheet(params);

	updateStockPriceHistory(params);
	
	
    }
    
    private static void updateStockAndCategory(Map<String, Object> params) {
	
	StockCategoryCsvHandler categoryCsvHandler = new StockCategoryCsvHandler();
	
	StockCsvHandler stockCsvHandler = new StockCsvHandler();
	
	categoryCsvHandler.setNextHandler(stockCsvHandler);
	
	categoryCsvHandler.executeTask(params);

    }

    private static void updateSheet(Map<String, Object> params) {
	
	StockEpsCsvHandler epsCsvHandler = new StockEpsCsvHandler();
	
	StockDividendCsvHandler dividendCsvHandler = new StockDividendCsvHandler();
	
	epsCsvHandler.setNextHandler(dividendCsvHandler);
	
	epsCsvHandler.executeTask(params);
	
    }

    private static void updateIncomeStatement(Map<String, Object> params) {
	
	StockIncomeStatementCsvHandler incomeStatementCsvHandler = new StockIncomeStatementCsvHandler();
	
	incomeStatementCsvHandler.executeTask(params);
	
    }

    private static void updateBalanceSheet(Map<String, Object> params) {
	
	StockBalanceSheetCsvHandler balanceSheetCsvHandler = new StockBalanceSheetCsvHandler();
	
	balanceSheetCsvHandler.executeTask(params);
	
    }

    private static void updateStockPriceHistory(Map<String, Object> params) {
	
	StockPriceHistoryCsvHandler stockPriceCsvHandler = new StockPriceHistoryCsvHandler();
	
	stockPriceCsvHandler.executeTask(params);
	
    }
    
    

}
