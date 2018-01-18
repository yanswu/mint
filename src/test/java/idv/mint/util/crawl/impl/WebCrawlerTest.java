package idv.mint.util.crawl.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import idv.mint.entity.enums.StockMarketType;
import idv.mint.util.crawl.Crawler;

public class WebCrawlerTest {

    @Test
    public void testGetStockCategoryTse() throws Exception {

	Crawler crawler = Crawler.createWebCrawler();
	List<String> list = crawler.getStockCategory(StockMarketType.TSE);
//	list.stream().forEach(System.out::println);
	assertFalse(list.isEmpty());
	assertEquals(list.get(0), "1,1,水泥");
	assertEquals(list.get(1), "1,2,食品");
	assertEquals(list.get(27), "1,28,其他");
	assertEquals(28, list.size());
    }
    @Test
    public void testGetStockCategoryOtc() throws Exception {
	
	Crawler crawler = Crawler.createWebCrawler();
	List<String> list = crawler.getStockCategory(StockMarketType.OTC);
//	list.stream().forEach(System.out::println);
	assertFalse(list.isEmpty());
	assertEquals(list.get(0), "2,1,食品");
	assertEquals(list.get(1), "2,2,塑膠");
	assertEquals(list.get(24), "2,25,管理");
	assertEquals(25, list.size());
    }

    @Test
    public void testGetStockTse() throws Exception {
	
	Crawler crawler = Crawler.createWebCrawler();
	List<String> lines = crawler.getStock(StockMarketType.TSE, "水泥");
//	lines.forEach(System.out::println);
	assertFalse(lines.isEmpty());
	assertEquals(lines.get(0),"1101,台泥");
	assertEquals(lines.get(1),"1102,亞泥");
	assertEquals(lines.get(2),"1103,嘉泥");
	assertEquals(lines.get(3),"1104,環泥");
	assertEquals(lines.get(4),"1108,幸福");
	assertEquals(lines.get(5),"1109,信大");
	assertEquals(lines.get(6),"1110,東泥");
	assertEquals(7, lines.size());
    }
    
    @Test
    public void testGetStockOtc() throws Exception {
	
	Crawler crawler = Crawler.createWebCrawler();
	List<String> lines = crawler.getStock(StockMarketType.OTC, "食品");
//	lines.forEach(System.out::println);
	assertFalse(lines.isEmpty());
	assertEquals(lines.get(0),"1258,其祥-KY");
	assertEquals(lines.get(1),"1264,德麥");
	assertEquals(lines.get(2),"4205,中華食");
	assertEquals(lines.get(3),"4207,環泰");
	assertEquals(lines.get(4),"4712,南璋");
	assertEquals(5, lines.size());
    }
    
    @Test
    public void testGetStockEPS() throws Exception {

	String stockCode = "1773";

	Crawler crawler = Crawler.createWebCrawler();
	List<String> list = crawler.getStockEPS(stockCode);
	
	String line = list.get(0);
	String[] sections = StringUtils.split(line, ",");
	
	assertEquals(stockCode, sections[0]);
	assertEquals(stockCode, sections[1]);
	assertEquals("2010", sections[2]);
	assertEquals("1.1", sections[3]);
	assertEquals("1.45", sections[4]);
	assertEquals("0.99", sections[5]);
	assertEquals("1.33", sections[6]);
	assertFalse(list.isEmpty());
	assertNotNull(list);
    }

    @Test
    public void testGetStockDividend() throws Exception {

	String stockCode = "1773";

	Crawler crawler = Crawler.createWebCrawler();
	List<String> list = crawler.getStockDividend(stockCode);
	
	String line = list.get(0);
	String[] sections = StringUtils.split(line, ",");
	
	assertEquals(stockCode, sections[0]);
	assertEquals("105", sections[1]);
	assertEquals("4.00", sections[2]);
	assertEquals("0.00", sections[3]);
	assertFalse(list.isEmpty());
	assertNotNull(list);
    }
    
    @Test
    public void testGetStock() throws IOException {
	
	Crawler crawler = Crawler.createWebCrawler();
	List<String> stockList = crawler.getStock(StockMarketType.OTC, "生技");
	stockList.forEach(line->{
	    String[] sections = StringUtils.split(line, ",");
	    if(StringUtils.equals(sections[0], "4157")) {
		assertEquals("太景*-KY", sections[1]);
	    }
	    
	});
    }
}
