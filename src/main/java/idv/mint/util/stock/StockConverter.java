package idv.mint.util.stock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.chrono.MinguoDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import idv.mint.bean.Stock;
import idv.mint.bean.StockCategory;
import idv.mint.bean.StockSheet;
import idv.mint.context.enums.SymbolType;
import idv.mint.entity.enums.StockMarketType;

public class StockConverter {
	
    protected static final Logger logger = LogManager.getLogger(StockConverter.class);

	// ,
	private static String comma = SymbolType.COMMA.getValue();
	// -
	private static String dash = SymbolType.DASH.getValue();

	public static List<StockCategory> convertStockCategoryList(List<String> lines) {

		List<StockCategory> list = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(lines)) {

			list = lines.stream()
					.map(line -> {

						String[] sections = StringUtils.split(line, comma);

						String marketTypeStr = sections[0];
						String orderNo = sections[1];
						String categoryName = sections[2];
						String stockCategoryId = (marketTypeStr + orderNo);

						StockCategory category = new StockCategory();
						category.setMarketType(StockMarketType.find(Integer.parseInt(marketTypeStr)));
						category.setOrderNo(Integer.parseInt(orderNo));
						category.setName(categoryName);
						category.setStockCategoryId(stockCategoryId);
						return category;
					})
					.collect(Collectors.toList());
		}
		return list;
	}

	/**
	 * <pre>
	 * 	pattern : 1,14,半導體,2330,台積電
	 * </pre>
	 * 
	 * @param lines
	 * @return */
	public static List<Stock> convertStockList(List<String> lines) {

		List<Stock> list = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(lines)) {

			list = lines.stream()
					.map(line -> {

						String[] sections = StringUtils.split(line, comma);

						String marketTypeStr = sections[0];
						String orderNo = sections[1];
						String categoryName = sections[2];
						String stockCode = sections[3];
						String stockName = sections[4];

						Stock stock = new Stock();
						stock.setStockCode(stockCode);
						stock.setStockName(stockName);

						StockCategory stockCategory = new StockCategory();
						String stockCategoryId = (marketTypeStr + orderNo);

						stockCategory.setMarketType(StockMarketType.find(Integer.parseInt(marketTypeStr)));
						stockCategory.setOrderNo(Integer.parseInt(orderNo));
						stockCategory.setName(categoryName);
						stockCategory.setStockCategoryId(stockCategoryId);

						stock.setStockCategory(stockCategory);

						return stock;

					})
					.collect(Collectors.toList());
		}
		return list;
	}

	public static StockSheet createStockSheetEps(String stockEpsLine) {

		List<String> stockCodeEpsLines = new ArrayList<>();
		stockCodeEpsLines.add(stockEpsLine);
		return createStockSheetList(stockCodeEpsLines, null,null,null).get(0);
	}

	public static StockSheet createStockSheetDividend(String stockDividendLine) {

		List<String> stockDividendLines = new ArrayList<>();
		stockDividendLines.add(stockDividendLine);
		return createStockSheetList(null, stockDividendLines,null,null).get(0);
	}

	private static Map<Integer, StockSheet> convertEpsMap(List<String> stockEPSLines) {

		List<StockSheet> list = new ArrayList<>();

		for (String line : stockEPSLines) {
			// stockCode, pageStockCode, year, q1, q2, q3, q4
			String[] sections = StringUtils.split(line, comma);
			String stockCode = sections[0];
			String htmlParserStockCode = sections[1];

			if (StringUtils.equals(stockCode, htmlParserStockCode)) {

				Integer year = Integer.parseInt(sections[2]);
				String epsQ1 = StringUtils.remove(sections[3], dash);
				String epsQ2 = StringUtils.remove(sections[4], dash);
				String epsQ3 = StringUtils.remove(sections[5], dash);
				String epsQ4 = StringUtils.remove(sections[6], dash);

				StockSheet stockSheet = new StockSheet(htmlParserStockCode, LocalDate.of(year, Month.JANUARY, 1));

				if (NumberUtils.isNumber(epsQ1)) {
					stockSheet.setEpsQ1(new BigDecimal(epsQ1));
				}
				if (NumberUtils.isNumber(epsQ2)) {
					stockSheet.setEpsQ2(new BigDecimal(epsQ2));
				}
				if (NumberUtils.isNumber(epsQ3)) {
					stockSheet.setEpsQ3(new BigDecimal(epsQ3));
				}
				if (NumberUtils.isNumber(epsQ4)) {
					stockSheet.setEpsQ4(new BigDecimal(epsQ4));
				}
				list.add(stockSheet);
			}
		}

		Map<Integer, StockSheet> epsMap = list.stream()
				.collect(Collectors.toMap(e -> e.getBaseDate()
						.getYear(), Function.identity()));

		return epsMap.entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

	}

	/**
	 * <pre>
	 *  依 stockCode 為單位 取 對應的 epsLines,dividendLines
	 * </pre>
	 * 
	 * @param stockEPSLines
	 * @param stockDividendLines
	 * @return */
	public static List<StockSheet> createStockSheetList(List<String> stockEPSLines, List<String> stockDividendLines, List<String> incomeStatementLines, List<String> balanceSheetLines) {

		// 1. 以EPS 為 Base
		Map<Integer, StockSheet> stockMappingMap = convertEpsMap(stockEPSLines);

		// 2. stock dividend
		if (CollectionUtils.isNotEmpty(stockDividendLines)) {

			int sysYear = LocalDate.now()
					.getYear();

			for (String line : stockDividendLines) {

				String[] sections = StringUtils.split(line, comma);
				String stockCode = sections[0];
				String rocYear = sections[1];
				String cashDividend = sections[2];
				String stockDividend = sections[3];

				// 1.先轉換成西元年
				// 2.yahoo 依 所屬年度 但系統要記錄是(發放年度)
				int payedYear = Integer.parseInt(rocYear) + 1;
				Integer y2kYear = LocalDate.from(MinguoDate.of(payedYear, 1, 1))
						.getYear();
				StockSheet stockSheet = stockMappingMap.get(y2kYear);

				// 代表 當年度 沒有對應的 該年度 EPS
				if (stockSheet == null && sysYear == y2kYear) {
					stockSheet = new StockSheet(stockCode, LocalDate.of(y2kYear, Month.JANUARY, 1));
					stockMappingMap.put(y2kYear, stockSheet);
				}
				// 重新再讀取 stockSheet
				stockSheet = stockMappingMap.get(y2kYear);

				if (stockSheet != null && StringUtils.equals(stockSheet.getStockCode(), stockCode)) {

					if (StringUtils.isNotBlank(cashDividend) && NumberUtils.isNumber(cashDividend)) {
						stockSheet.setCashDividend(new BigDecimal(cashDividend));
					}

					if (StringUtils.isNotBlank(stockDividend) && NumberUtils.isNumber(stockDividend)) {
						stockSheet.setStockDividend(new BigDecimal(stockDividend));
					}
				}
			}
		}

		// 3.
		if (CollectionUtils.isNotEmpty(incomeStatementLines)) {

			for (String line : incomeStatementLines) {

				String[] sections = StringUtils.split(line, comma);
				String stockCode = sections[0];
				String rocYear = sections[1];
				String netIncome = sections[2];

				int y2kYear = LocalDate.from(MinguoDate.of(Integer.parseInt(rocYear), 1, 1)).getYear();
				
				StockSheet stockSheet = stockMappingMap.get(y2kYear);

				if (stockSheet != null && stockSheet.getStockCode().equals(stockCode)) {
					if(NumberUtils.isNumber(netIncome)) {
						stockSheet.setNetIncome(new BigDecimal(netIncome));
					}
				}
			}
		}

		// 4.
		if (CollectionUtils.isNotEmpty(balanceSheetLines)) {

			for (String line : balanceSheetLines) {
				
//				logger.debug(line);
				
				String[] sections = StringUtils.split(line, comma);
				String stockCode = sections[0];
				String rocYear = sections[1];
				// 長期投資,固定資產,股東權益
				String longTermInvest = sections[2];
				String fixedAsset = sections[3];
				String shareholderEquity = sections[4];

				int y2kYear = LocalDate.from(MinguoDate.of(Integer.parseInt(rocYear), 1, 1)).getYear();
				
				StockSheet stockSheet = stockMappingMap.get(y2kYear);

				if (stockSheet != null && stockSheet.getStockCode().equals(stockCode)) {
					
					if(NumberUtils.isNumber(longTermInvest)) {
						stockSheet.setLongTermInvest(new BigDecimal(longTermInvest));
					}
					if(NumberUtils.isNumber(fixedAsset)) {
						stockSheet.setFixedAsset(new BigDecimal(fixedAsset));
					}
					if(NumberUtils.isNumber(shareholderEquity)) {
						stockSheet.setShareholderEquity(new BigDecimal(shareholderEquity));
					}
				}
			}
		}

		return stockMappingMap.values()
				.stream()
				.collect(Collectors.toList());
		// return new ArrayList<>(stockMappingMap.values());
	}
}
