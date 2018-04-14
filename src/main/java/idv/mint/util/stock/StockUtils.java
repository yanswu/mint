package idv.mint.util.stock;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;

import idv.mint.bean.StockSheet;

public class StockUtils {

    /**
     * 計算還原股價
     * 
     * @param stockPrice
     * @param sheetList
     */
    public static void calculateValuePrice(BigDecimal stockPrice, List<StockSheet> sheetList) {

	for (int i = 0; i < sheetList.size(); i++) {

	    StockSheet stockSheet = sheetList.get(i);

	    List<StockSheet> subList = sheetList.subList(i, sheetList.size());

	    // 計算股數
	    BigDecimal totalShares = BigDecimal.ONE;
	    // 計算現金股利
	    BigDecimal totalCash = BigDecimal.ZERO;

	    for (StockSheet sheet : subList) {

		BigDecimal shares = ObjectUtils.defaultIfNull(sheet.getStockDividend(), BigDecimal.ZERO);
		BigDecimal cash = ObjectUtils.defaultIfNull(sheet.getCashDividend(), BigDecimal.ZERO);
		
		// 股利  = 現金 * 股子
		totalCash = totalCash.add(cash.multiply(totalShares));
		
		// 下期股子 = 本期股子 + (當期股子/10)
		totalShares = totalShares.add((shares.divide(new BigDecimal(10))));
	    }
	    
	    // 還原價值
	    BigDecimal valuePrice = stockPrice.divide(totalShares).add(totalCash);
	    stockSheet.setValuePrice(valuePrice);
	}
    }



    public static void main(String[] args) {

	System.err.println(BigDecimal.ZERO.divide(new BigDecimal(10)).toString());
	System.err.println(BigDecimal.ONE.divide(new BigDecimal(10)).toString());
    }
}
