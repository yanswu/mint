package idv.mint.util.stock;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import idv.mint.bean.StockCategory;

public class StockCreatorTest {

    @Test
    public void testCreateStockCategoryList() throws Exception {
	
	List<String> lines = new ArrayList<String>();
	lines.add("1,6,TseCategoryName");
	lines.add("2,7,OtcCategoryName");
	
	List<StockCategory> list = StockCreator.createStockCategoryList(lines);
	StockCategory tseStockCategory = list.get(0);
	StockCategory otcStockCategory = list.get(1);
	
	assertTrue(tseStockCategory.getMarketType().isTSE());
	assertTrue(tseStockCategory.getOrderNo() == 6);
	assertTrue(StringUtils.equals(tseStockCategory.getName(), "TseCategoryName"));

	assertTrue(otcStockCategory.getMarketType().isOTC());
	assertTrue(otcStockCategory.getOrderNo() == 7);
	assertTrue(StringUtils.equals(otcStockCategory.getName(), "OtcCategoryName"));
	
	
    }

    @Test
    public void testCreateStockList() throws Exception {
	throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testCreateStockSheetEpsList() throws Exception {
	throw new RuntimeException("not yet implemented");
    }

}
