package idv.mint.util.stock;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import idv.mint.bean.Stock;
import idv.mint.bean.StockCategory;
import idv.mint.bean.StockSheet;
import idv.mint.entity.enums.StockMarketType;

public class StockCreator {
    
    public static List<StockCategory> createStockCategoryList(List<String> lines) {

 	List<StockCategory> list = new ArrayList<>();

 	if (!CollectionUtils.isEmpty(lines)) {

 	    list = lines.stream().map(line -> {
 		String[] sections = StringUtils.split(line, ",");
 		StockMarketType marketType = StockMarketType.find(Integer.parseInt(sections[0]));
 		return new StockCategory(marketType, Integer.parseInt(sections[1]), sections[2]);
 	    }).collect(Collectors.toList());
 	}
 	return list;
     }

    public static List<Stock> createStockList(List<String> lines) {
	
	List<Stock> list = new ArrayList<>();
	
	if (!CollectionUtils.isEmpty(lines)) {
	    
	    list = lines.stream().map(line -> {
		String[] sections = StringUtils.split(line, ",");
		return new Stock(sections[3], sections[4]);
	    }).collect(Collectors.toList());
	}
	return list;
    }

     /**
      * 
      * @param epsLines
      * @param dividendLines
      * @return
      */
     public static List<StockSheet> createStockSheetEpsList(List<String> epsLines, List<String> dividendLines) {

 	List<StockSheet> list = new ArrayList<>();

 	if (!CollectionUtils.isEmpty(epsLines)) {

 	    for (String line : epsLines) {
 		String[] sections = StringUtils.split(line, ",");
 		// stockCode, pageStockCode, year, q1, q2, q3, q4
 		String stockCode = sections[0];
 		String htmlParserStockCode = sections[1];

 		if (StringUtils.equals(stockCode, htmlParserStockCode)) {

 		    Integer year = Integer.parseInt(sections[2]);
 		    String epsQ1 = StringUtils.remove(sections[3], "-");
 		    String epsQ2 = StringUtils.remove(sections[4], "-");
 		    String epsQ3 = StringUtils.remove(sections[5], "-");
 		    String epsQ4 = StringUtils.remove(sections[6], "-");

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
 	    if (!CollectionUtils.isEmpty(dividendLines)) {

 		dividendLines.forEach(line -> {
 		    String[] sections = StringUtils.split(line, ",");
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
