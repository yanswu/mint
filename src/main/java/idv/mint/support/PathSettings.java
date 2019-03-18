package idv.mint.support;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import idv.mint.entity.enums.StockMarketType;

public enum PathSettings {

	/** category */
	STOCK_CATEGORY_TSE_CSV(StockMarketType.TSE, Types.CATEGORY, "stockCategoryTse.csv"),

	STOCK_CATEGORY_OTC_CSV(StockMarketType.OTC, Types.CATEGORY, "stockCategoryOtc.csv"),

	/** stock */
	STOCK_TSE_CSV(StockMarketType.TSE, Types.STOCK, "stockTse.csv"),

	STOCK_OTC_CSV(StockMarketType.OTC, Types.STOCK, "stockOtc.csv"),

	/** eps */
	STOCK_EPS_TSE_CSV(StockMarketType.TSE, Types.EPS, "stockEpsTse.csv"),

	STOCK_EPS_OTC_CSV(StockMarketType.OTC, Types.EPS, "stockEpsOtc.csv"),

	/** cash dividend stock dividend */
	STOCK_DIVIDEND_TSE_CSV(StockMarketType.TSE, Types.DIVIDEND, "stockDividendTse.csv"),

	STOCK_DIVIDEND_OTC_CSV(StockMarketType.OTC, Types.DIVIDEND, "stockDividendOtc.csv"),

	/** stock incomeStatement */
	STOCK_INCOMESTATEMENT_TSE_CSV(StockMarketType.TSE, Types.INCOMESTATEMENT, "stockIncomeStatementTse.csv"),

	STOCK_INCOMESTATEMENT_OTC_CSV(StockMarketType.OTC, Types.INCOMESTATEMENT, "stockIncomeStatementOtc.csv"),

	/** stock balanceSheet */
	STOCK_BALANCESHEET_TSE_CSV(StockMarketType.TSE, Types.BALANCESHEET, "stockBalanceSheetTse.csv"),

	STOCK_BALANCESHEET_OTC_CSV(StockMarketType.OTC, Types.BALANCESHEET, "stockBalanceSheetOtc.csv"),

	/** stock price history */
	STOCK_PRICE_HISTORY_TSE_CSV(StockMarketType.TSE, Types.PRICE_HISTORY, "stockPriceHistoryTse.csv"),

	STOCK_PRICE_HISTORY_OTC_CSV(StockMarketType.OTC, Types.PRICE_HISTORY, "stockPriceHistoryOtc.csv"),

	UNKNOWN_CSV(StockMarketType.UNKNOWN, Types.UNKNOWN, "unknown.csv");

	public enum Types {

		CATEGORY, STOCK, EPS, DIVIDEND, INCOMESTATEMENT, BALANCESHEET, PRICE_HISTORY,UNKNOWN
	}

	private String STOCK_FILE_FOLDER = "src/main/resources/data/stock";

	private StockMarketType marketType;
	private Types type;
	private Path path;
	private String uri;

	PathSettings(StockMarketType marketType, Types type, String uri) {
		this.marketType = marketType;
		this.type = type;
		this.uri = uri;
	}

	public static PathSettings find(StockMarketType marketType,Types type) {
		
		return Arrays.asList(PathSettings.values()).stream().filter(setting ->{
			return(setting.getMarketType() == marketType && setting.getType() == type);
		}).findFirst().orElse(UNKNOWN_CSV);
		
	}

	public Path getPath() {
		this.path = Paths.get(STOCK_FILE_FOLDER, uri);
		return path;
	}

	public String getUri() {
		return uri;
	}

	public StockMarketType getMarketType() {
		return marketType;
	}

	public Types getType() {
		return type;
	}

}
