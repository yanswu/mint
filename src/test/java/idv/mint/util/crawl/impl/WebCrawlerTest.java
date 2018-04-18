package idv.mint.util.crawl.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import idv.mint.context.enums.SymbolType;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.util.crawl.Crawler;

public class WebCrawlerTest {

    private static String comma = SymbolType.COMMA.getValue();

    @Test
    public void testGetStockCategoryLinesTse() throws Exception {

	Crawler crawler = Crawler.createWebCrawler();
	List<String> list = crawler.getStockCategoryLines(StockMarketType.TSE);
	// list.stream().forEach(System.out::println);
	assertFalse(list.isEmpty());
	assertEquals(list.get(0), "1,1,水泥");
	assertEquals(list.get(1), "1,2,食品");
	assertEquals(list.get(27), "1,28,其他");
	assertEquals(28, list.size());
    }

    @Test
    public void testGetStockCategoryLinesOtc() throws Exception {

	Crawler crawler = Crawler.createWebCrawler();
	List<String> list = crawler.getStockCategoryLines(StockMarketType.OTC);
	// list.stream().forEach(System.out::println);
	assertFalse(list.isEmpty());
	assertEquals(list.get(0), "2,1,食品");
	assertEquals(list.get(1), "2,2,塑膠");
	assertEquals(list.get(24), "2,25,管理");
	assertEquals(25, list.size());
    }

    @Test
    public void testGetStockLinesTse() throws Exception {

	Crawler crawler = Crawler.createWebCrawler();
	List<String> lines = crawler.getStockLines(StockMarketType.TSE, "水泥");
	// lines.forEach(System.out::println);
	assertFalse(lines.isEmpty());
	assertEquals(lines.get(0), "1101,台泥");
	assertEquals(lines.get(1), "1102,亞泥");
	assertEquals(lines.get(2), "1103,嘉泥");
	assertEquals(lines.get(3), "1104,環泥");
	assertEquals(lines.get(4), "1108,幸福");
	assertEquals(lines.get(5), "1109,信大");
	assertEquals(lines.get(6), "1110,東泥");
	assertEquals(7, lines.size());
    }

    @Test
    public void testGetStockLinesOtc() throws Exception {

	Crawler crawler = Crawler.createWebCrawler();
	List<String> lines = crawler.getStockLines(StockMarketType.OTC, "食品");
	// lines.forEach(System.out::println);
	assertFalse(lines.isEmpty());
	assertEquals(lines.get(0), "1258,其祥-KY");
	assertEquals(lines.get(1), "1264,德麥");
	assertEquals(lines.get(2), "1796,金穎生技");
	assertEquals(lines.get(3), "4205,中華食");
	assertEquals(lines.get(4), "4207,環泰");
	assertEquals(lines.get(5), "4712,南璋");
	assertEquals(6, lines.size());
    }

    @Test
    public void testGetStockLinesEPSLines() throws Exception {

	String stockCode = "1773";

	Crawler crawler = Crawler.createWebCrawler();
	List<String> lines = crawler.getStockEPSLines(stockCode);

	assertNotNull(lines);

	lines.stream().forEach(line -> {

	    String[] sections = StringUtils.split(line, ",");

	    String pageStockCode = sections[1];
	    String y2kYear = sections[2];

	    assertEquals(stockCode, sections[0]);
	    assertEquals(stockCode, pageStockCode);

	    if ("2010".equals(y2kYear)) {
		assertEquals("1.1", sections[3]);
		assertEquals("1.45", sections[4]);
		assertEquals("0.99", sections[5]);
		assertEquals("1.33", sections[6]);
	    }

	});
    }

    @Test
    public void testGetStockLinesDividendLines() throws Exception {

	String stockCode = "1773";

	Crawler crawler = Crawler.createWebCrawler();
	List<String> lines = crawler.getStockDividendLines(stockCode);

	assertNotNull(lines);
	assertFalse(lines.isEmpty());

	lines.stream().forEach(line -> {

	    String[] sections = StringUtils.split(line, ",");
	    String rocYear = sections[1];
	    String cashDividends = sections[2];
	    String stockDividends = sections[3];

	    assertEquals(stockCode, sections[0]);

	    if ("106".equals(rocYear)) {
		assertEquals("4.60", cashDividends);
		assertEquals("0.00", stockDividends);
	    }

	    if ("105".equals(rocYear)) {
		assertEquals("4.00", cashDividends);
		assertEquals("0.00", stockDividends);
	    }
	});
    }

    @Test
    public void testGetStockLines() throws IOException {

	Crawler crawler = Crawler.createWebCrawler();
	List<String> lines = crawler.getStockLines(StockMarketType.OTC, "生技");
	lines.stream().forEach(System.out::println);

	lines.stream().forEach(line -> {
	    String[] sections = StringUtils.split(line, ",");
	    if (StringUtils.equals(sections[0], "4157")) {
		assertEquals("太景*-KY", sections[1]);
	    }

	});
    }

    @Test
    public void testGetStockNameByHiStock() throws IOException {

	String stockCode = "4157";
	String stockName = new WebCrawler().getStockNameByHiStock(stockCode);
	assertEquals("太景*-KY", stockName);
    }

    @Test
    public void testGetStockPrice() throws IOException {

	String stockCode = "1773";
	Crawler crawler = Crawler.createWebCrawler();
	String price = crawler.getStockPrice(stockCode);
	assertNotNull(price);
    }

    @Test
    public void testGetIncomeStatementLines() throws IOException {

	String stockCode = "1773";
	Crawler crawler = Crawler.createWebCrawler();
	List<String> lines = crawler.getIncomeStatementLines(stockCode);

	assertNotNull(lines);

	lines.stream().forEach(line -> {

	    String[] sections = line.split(comma);
	    String rocYear = sections[1];
	    String netIncome = sections[2];

	    
	    if (StringUtils.equals("106", rocYear)) {
		assertEquals("879", netIncome);
	    }

	    if (StringUtils.equals("105", rocYear)) {
		assertEquals("785", netIncome);
	    }

	    if (StringUtils.equals("99", rocYear)) {
		assertEquals("650", netIncome);
	    }
	});
    }

    @Test
    public void testGetBalanceSheetLines() throws IOException {

	String stockCode = "1773";
	Crawler crawler = Crawler.createWebCrawler();
	List<String> lines = crawler.getBalanceSheetLines(stockCode);

	assertNotNull(lines);

	lines.stream().forEach(line -> {

	    String[] sections = line.split(comma);

	    String rocYear = sections[1];
	    String longTermInvest = sections[2];
	    String fixedAsset = sections[3];
	    String shareHolderEquity = sections[4];
	    
	    if (StringUtils.equals("106", rocYear)) {
		assertEquals("1095", longTermInvest);
		assertEquals("1709", fixedAsset);
		assertEquals("4503", shareHolderEquity);
	    }

	    if (StringUtils.equals("105", rocYear)) {
		assertEquals("1123", longTermInvest);
		assertEquals("1514", fixedAsset);
		assertEquals("4251", shareHolderEquity);
	    }

	    if (StringUtils.equals("99", rocYear)) {
		assertEquals("841", longTermInvest);
		assertEquals("1336", fixedAsset);
		assertEquals("3234", shareHolderEquity);
	    }
	});
    }

}
