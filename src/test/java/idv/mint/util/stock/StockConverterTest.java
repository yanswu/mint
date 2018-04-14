package idv.mint.util.stock;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import idv.mint.bean.StockCategory;
import idv.mint.bean.StockSheet;

public class StockConverterTest {

    @Test
        public void testConvertStockCategoryList() throws Exception {
    	
    	List<String> lines = new ArrayList<String>();
    	lines.add("1,6,TseCategoryName");
    	lines.add("2,7,OtcCategoryName");
    	
    	List<StockCategory> list = StockConverter.convertStockCategoryList(lines);
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
        public void testConvertStockList() throws Exception {
    	
    	List<String> stockEPSLines = new ArrayList<>();
    	stockEPSLines.add("1773,1773,2015,0.9,1.02,1.06,1.1");
    	stockEPSLines.add("1773,1773,2016,1.12,1.35,1.33,1.43");
    	
    	List<String> stockDividendLines = new ArrayList<>();
    	stockDividendLines.add("1773,105,4.00,0.00");
    	stockDividendLines.add("1773,104,3.00,0.00");
    	
    	List<StockSheet> stockSheetList = StockConverter.createStockSheetList(stockEPSLines, stockDividendLines);
    	
    	stockSheetList.forEach(e->{
    	    
    	    int year = e.getBaseDate().getYear();
    	    if (2015 == year) {
    		    Assert.assertThat(e.getEpsQ1(), Matchers.comparesEqualTo(new BigDecimal("0.9")));
    		    Assert.assertThat(e.getEpsQ2(), Matchers.comparesEqualTo(new BigDecimal("1.02")));
    		    Assert.assertThat(e.getEpsQ3(), Matchers.comparesEqualTo(new BigDecimal("1.06")));
    		    Assert.assertThat(e.getEpsQ4(), Matchers.comparesEqualTo(new BigDecimal("1.1")));
    		    Assert.assertThat(e.getCashDividend(), Matchers.comparesEqualTo(new BigDecimal(3)));
    		    Assert.assertThat(e.getStockDividend(), Matchers.comparesEqualTo(BigDecimal.ZERO));
    		}
    		if (2016 == year) {
    		    Assert.assertThat(e.getEpsQ1(), Matchers.comparesEqualTo(new BigDecimal("1.12")));
    		    Assert.assertThat(e.getEpsQ2(), Matchers.comparesEqualTo(new BigDecimal("1.35")));
    		    Assert.assertThat(e.getEpsQ3(), Matchers.comparesEqualTo(new BigDecimal("1.33")));
    		    Assert.assertThat(e.getEpsQ4(), Matchers.comparesEqualTo(new BigDecimal("1.43")));
    		    Assert.assertThat(e.getCashDividend(), Matchers.comparesEqualTo(new BigDecimal(4)));
    		    Assert.assertThat(e.getStockDividend(), Matchers.comparesEqualTo(BigDecimal.ZERO));
    		}
    	});
    	
        }



}
