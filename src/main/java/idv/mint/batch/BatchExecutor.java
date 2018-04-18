package idv.mint.batch;

import java.util.HashMap;
import java.util.Map;

import idv.mint.batch.executor.StockBalanceSheetCsvHandler;
import idv.mint.batch.executor.StockCategoryCsvHandler;
import idv.mint.batch.executor.StockCsvHandler;
import idv.mint.batch.executor.StockDividendCsvHandler;
import idv.mint.batch.executor.StockEpsCsvHandler;
import idv.mint.batch.executor.StockIncomeStatementCsvHandler;

public class BatchExecutor {
    
    public static void main(String[] args) throws Exception {
	
	Map<String, Object> params = new HashMap<>();
	
	// 
//	updateStockAndCategory(params);
	
//	updateSheet(params);

//	updateIncomeStatement(params);
	
	updateBalanceSheet(params);
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
    
    

}
