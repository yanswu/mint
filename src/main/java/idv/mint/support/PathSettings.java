package idv.mint.support;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum PathSettings {
    
    /**category */
    STOCK_CATEGORY_TSE_CSV("stockCategoryTse.csv"),
    
    STOCK_CATEGORY_OTC_CSV("stockCategoryOtc.csv"),

    /**stock */
    STOCK_TSE_CSV("stockTse.csv"),
    
    STOCK_OTC_CSV("stockOtc.csv"),
    
    /**eps */
    STOCK_EPS_TSE_CSV("stockEpsTse.csv"),
    
    STOCK_EPS_OTC_CSV("stockEpsOtc.csv"),
    
    /**cash dividend stock dividend */
    STOCK_DIVIDEND_TSE_CSV("stockDividendTse.csv"),
    
    STOCK_DIVIDEND_OTC_CSV("stockDividendOtc.csv"),

    /** stock incomeStatement */
    STOCK_INCOMESTATEMENT_TSE_CSV("stockIncomeStatementTse.csv"),
    
    STOCK_INCOMESTATEMENT_OTC_CSV("stockIncomeStatementOtc.csv"),

    /** stock balanceSheet */
    STOCK_BALANCESHEET_TSE_CSV("stockBalanceSheetTse.csv"),
    
    STOCK_BALANCESHEET_OTC_CSV("stockBalanceSheetOtc.csv"),

    /** stock price history */
    STOCK_PRICE_HISTORY_TSE_CSV("stockPriceHistoryTse.csv"),
    
    STOCK_PRICE_HISTORY_OTC_CSV("stockPriceHistoryOtc.csv");
    

    private String STOCK_FILE_FOLDER = "src/main/resources/data/stock";

    private Path path;
    private String uri;
    
    PathSettings(String uri) {
	this.uri = uri;
    }

    public Path getPath() {
	this.path = Paths.get(STOCK_FILE_FOLDER,uri);	    
	return path;
    }
    
    public String getUri() {
	return uri;
    }

}
