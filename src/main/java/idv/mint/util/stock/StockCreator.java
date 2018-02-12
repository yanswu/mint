package idv.mint.util.stock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import idv.mint.bean.Stock;
import idv.mint.bean.StockCategory;
import idv.mint.bean.StockSheet;
import idv.mint.context.enums.SymbolType;
import idv.mint.entity.enums.StockMarketType;

public class StockCreator {

    public static List<StockCategory> createStockCategoryList(List<String> lines) {

	List<StockCategory> list = new ArrayList<>();

	if (CollectionUtils.isNotEmpty(lines)) {

	    String comma = SymbolType.COMMA.getValue();

	    list = lines.stream().map(line -> {

		String[] sections = StringUtils.split(line, comma);

		String marketType = sections[0];
		String orderNo = sections[1];
		String categoryName = sections[2];
		String stockCategoryId = (marketType + orderNo);

		StockCategory category = new StockCategory();
		category.setMarketType(StockMarketType.find(Integer.parseInt(marketType)));
		category.setOrderNo(Integer.parseInt(orderNo));
		category.setName(categoryName);
		category.setStockCategoryId(stockCategoryId);
		return category;
	    }).collect(Collectors.toList());
	}
	return list;
    }

    public static List<Stock> createStockList(List<String> lines) {

	List<Stock> list = new ArrayList<>();

	if (CollectionUtils.isNotEmpty(lines)) {

	    String comma = SymbolType.COMMA.getValue();

	    list = lines.stream().map(line -> {

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
		stockCategory.setMarketType(StockMarketType.find(Integer.parseInt(marketTypeStr)));
		stockCategory.setOrderNo(Integer.parseInt(orderNo));
		stockCategory.setName(categoryName);
		stockCategory.setStockCategoryId(marketTypeStr + orderNo);

		stock.setStockCategory(stockCategory);

		return stock;

	    }).collect(Collectors.toList());
	}
	return list;
    }

    /**
     * <pre>
     *  依 stockCode 為單位 取 對應的 epsLines,dividendLines
     * </pre>
     * 
     * @param stockCodeEpsLines
     * @param stockDividendLines
     * @return
     */
    public static List<StockSheet> createStockSheetEpsList(List<String> stockCodeEpsLines, List<String> stockDividendLines) {

	List<StockSheet> list = new ArrayList<>();

	if (CollectionUtils.isNotEmpty(stockCodeEpsLines)) {
	    
	    // ,
	    String comma = SymbolType.COMMA.getValue();
	    // -
	    String dash = SymbolType.DASH.getValue();

	    for (String line : stockCodeEpsLines) {
		String[] sections = StringUtils.split(line, comma);
		// stockCode, pageStockCode, year, q1, q2, q3, q4
		String stockCode = sections[0];
		String htmlParserStockCode = sections[1];

		if (StringUtils.equals(stockCode, htmlParserStockCode)) {

		    Integer year = Integer.parseInt(sections[2]);
		    String epsQ1 = StringUtils.remove(sections[3], dash);
		    String epsQ2 = StringUtils.remove(sections[4], dash);
		    String epsQ3 = StringUtils.remove(sections[5], dash);
		    String epsQ4 = StringUtils.remove(sections[6], dash);

		    StockSheet stockSheet = new StockSheet(htmlParserStockCode, LocalDate.of(year, 1, 1));

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

	    Map<Integer, StockSheet> map = list.stream().collect(Collectors.toMap(c -> c.getBaseDate().getYear(), Function.identity()));

	    // stock dividend
	    if (CollectionUtils.isNotEmpty(stockDividendLines)) {

		stockDividendLines.forEach(line -> {

		    String[] sections = StringUtils.split(line, comma);
		    String stockCode = sections[0];
		    String rocYear = sections[1];
		    String cashDividend = sections[2];
		    String stockDividend = sections[3];
		    // 1.先轉換成西元年
		    // 2.yahoo 依 所屬年度 , +1 為 發放年度
		    Integer y2kYear = Integer.parseInt(rocYear) + 1911 + 1;
		    StockSheet stockSheet = map.get(y2kYear);
		    if (stockSheet != null && StringUtils.equals(stockSheet.getStockCode(), stockCode)) {
			if (StringUtils.isNotBlank(cashDividend) && NumberUtils.isNumber(cashDividend)) {
			    stockSheet.setCashDividend(new BigDecimal(cashDividend));
			}
			if (StringUtils.isNotBlank(stockDividend) && NumberUtils.isNumber(stockDividend)) {
			    stockSheet.setStockDividend(new BigDecimal(stockDividend));
			}
		    }
		});
	    }
	}
	return list;
    }
}
