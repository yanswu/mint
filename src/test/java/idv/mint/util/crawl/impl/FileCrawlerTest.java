package idv.mint.util.crawl.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import idv.mint.bean.StockSheet;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.util.crawl.Crawler;
import idv.mint.util.stock.StockCreator;

public class FileCrawlerTest {

    @Test
    public void testGetStockLinesLinesCategory() throws IOException {

	Crawler crawler = Crawler.createFileCrawler();

	List<String> tseCategoryLines = crawler.getStockCategoryLines(StockMarketType.TSE);
	assertEquals(28, tseCategoryLines.size());

	List<String> otcCategoryLines = crawler.getStockCategoryLines(StockMarketType.OTC);
	assertEquals(25, otcCategoryLines.size());

	List<String> nonCategoryLines = crawler.getStockCategoryLines(StockMarketType.UNKNOWN);
	assertEquals(0, nonCategoryLines.size());

    }

    @Test
    public void testGetStockLinesLinesEPSTse() throws IOException {

	Crawler crawler = Crawler.createFileCrawler();

	String stockCode = "1773";
	List<String> epsLines = crawler.getStockEPSLines(stockCode);
	List<String> dividendLines = crawler.getStockDividendLines(stockCode);
	List<StockSheet> stockSheetList = StockCreator.createStockSheetEpsList(epsLines, dividendLines);

	stockSheetList.stream().forEach(sheet -> {
	    int year = sheet.getBaseDate().getYear();

	    if (2010 == year) {
		assertTrue(new BigDecimal("1.1").compareTo(sheet.getEpsQ1()) == 0);
		assertTrue(new BigDecimal("1.45").compareTo(sheet.getEpsQ2()) == 0);
		assertTrue(new BigDecimal("0.99").compareTo(sheet.getEpsQ3()) == 0);
		assertTrue(new BigDecimal("1.33").compareTo(sheet.getEpsQ4()) == 0);
		assertTrue(new BigDecimal("2.50").compareTo(sheet.getCashDividend()) == 0);
		assertTrue(new BigDecimal("0.00").compareTo(sheet.getStockDividend()) == 0);
	    }
	    if (2011 == year) {
		assertTrue(new BigDecimal("1.04").compareTo(sheet.getEpsQ1()) == 0);
		assertTrue(new BigDecimal("1.15").compareTo(sheet.getEpsQ2()) == 0);
		assertTrue(new BigDecimal("0.71").compareTo(sheet.getEpsQ3()) == 0);
		assertTrue(new BigDecimal("0.73").compareTo(sheet.getEpsQ4()) == 0);
		assertTrue(new BigDecimal("4.00").compareTo(sheet.getCashDividend()) == 0);
		assertTrue(new BigDecimal("0.0").compareTo(sheet.getStockDividend()) == 0);
	    }
	});
    }

    @Test
    public void testGetStockLinesLinesEPSOtc() throws IOException {

	Crawler crawler = Crawler.createFileCrawler();

	String stockCode = "6261";
	List<String> epsLines = crawler.getStockEPSLines(stockCode);
	List<String> dividendLines = crawler.getStockDividendLines(stockCode);
	List<StockSheet> stockSheetList = StockCreator.createStockSheetEpsList(epsLines, dividendLines);

	stockSheetList.stream().forEach(sheet -> {
	    int year = sheet.getBaseDate().getYear();

	    // 6261,6261,2010,1.73,2.5,2.35,1.95
	    // 6261,6261,2011,1.91,2,1.76,1.01
	    if (2010 == year) {

		assertTrue(new BigDecimal("1.73").compareTo(sheet.getEpsQ1()) == 0);
		assertTrue(new BigDecimal("2.5").compareTo(sheet.getEpsQ2()) == 0);
		assertTrue(new BigDecimal("2.35").compareTo(sheet.getEpsQ3()) == 0);
		assertTrue(new BigDecimal("1.95").compareTo(sheet.getEpsQ4()) == 0);

		assertTrue(new BigDecimal("3.00").compareTo(sheet.getCashDividend()) == 0);
		assertTrue(new BigDecimal("0.10").compareTo(sheet.getStockDividend()) == 0);
	    }
	    if (2011 == year) {
		assertTrue(new BigDecimal("1.91").compareTo(sheet.getEpsQ1()) == 0);
		assertTrue(new BigDecimal("2.00").compareTo(sheet.getEpsQ2()) == 0);
		assertTrue(new BigDecimal("1.76").compareTo(sheet.getEpsQ3()) == 0);
		assertTrue(new BigDecimal("1.01").compareTo(sheet.getEpsQ4()) == 0);

		assertTrue(new BigDecimal("4.67").compareTo(sheet.getCashDividend()) == 0);
		assertTrue(new BigDecimal("0.09").compareTo(sheet.getStockDividend()) == 0);
	    }
	});
    }
}
