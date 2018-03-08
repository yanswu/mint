package idv.mint.util.crawl.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import idv.mint.context.enums.EncodingType;
import idv.mint.context.enums.SymbolType;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.util.CharUtils;
import idv.mint.util.JsoupUtils;
import idv.mint.util.crawl.Crawler;

public class WebCrawler implements Crawler {

    private static final Logger logger = LogManager.getLogger(WebCrawler.class);

    private static final Integer RETRY_TIMES = 3;

    public WebCrawler() {
    }

    /**
     * <pre>
     * 	 pattern :marketType(1,2), sequence, categoryName
     * </pre>
     * 
     * @param marketType
     * @return
     * @throws IOException
     */
    @Override
    public List<String> getStockCategoryLines(StockMarketType marketType) throws IOException {

	String url = "https://tw.stock.yahoo.com/h/getclass.php#table1";
	Document document = JsoupUtils.getDocument(url);
	Elements tables = document.select(".font-be-adjust > table");

	final List<Element> htmlTDList;

	if (marketType.isTSE()) {
	    Element tseTable = tables.get(1);
	    htmlTDList = tseTable.select("td").subList(0, 28);

	} else if (marketType.isOTC()) {
	    Element otcTable = tables.get(3);
	    htmlTDList = otcTable.select("td").subList(0, 26);
	    htmlTDList.remove(23);// remove 指數類
	} else {
	    htmlTDList = null;
	}

	if (htmlTDList != null) {

	    String comma = SymbolType.COMMA.getValue();

	    return IntStream.range(0, htmlTDList.size()).mapToObj(i -> {
		String sequence = String.valueOf(i + 1);
		String categoryName = StringUtils.trimToEmpty(htmlTDList.get(i).text());
		return String.join(comma, String.valueOf(marketType.getValue()), sequence, categoryName);
	    }).collect(Collectors.toList());

	}

	return new ArrayList<>();
    }

    @Override
    public List<String> getStockLines(StockMarketType marketType) throws IOException {

	// pattern :marketType(1,2), sequence, categoryName
	List<String> stockCategoryLines = this.getStockCategoryLines(marketType);

	String comma = SymbolType.COMMA.getValue();

	List<String> stockLines = new ArrayList<>();

	for (String categoryLine : stockCategoryLines) {
	    String[] sections = StringUtils.split(categoryLine, comma);
	    String categoryName = sections[2];
	    // pattern : stockCode,stockName
	    List<String> lines = getStockLines(marketType, categoryName, RETRY_TIMES);
	    for (String line : lines) {
		StringBuilder sb = new StringBuilder();
		sb.append(categoryLine).append(comma).append(line);
		stockLines.add(sb.toString());
	    }
	}
	return stockLines;
    }

    @Override
    public List<String> getStockLines(StockMarketType marketType, String categoryName) throws IOException {
	// crate pattern : stockCode,stockName
	return getStockLines(marketType, categoryName, RETRY_TIMES);
    }

    public List<String> getStockLines(StockMarketType marketType, String categoryName, int reTryTimes) throws IOException {

	String urlTemplate = "https://tw.stock.yahoo.com/s/list.php?c=%s";
	String prefix = marketType.isOTC() ? "櫃" : "";
	String qCategoryName = URLEncoder.encode(prefix + categoryName, EncodingType.BIG5.getValue());
	String url = String.format(urlTemplate, qCategoryName);

	try {
	    Document document = JsoupUtils.getDocument(url);
	    Elements trList = document.select(" td table[bgcolor='#ffffff'] tr");

	    if (trList != null) {
		// start from second tr
		return trList.subList(1, trList.size()).stream().map(tr -> {
		    String text = StringUtils.trimToEmpty(tr.select("td").get(1).text());
		    String[] sections = StringUtils.split(text);
		    String stockCode = sections[0];
		    String stockName = sections[1];
		    String currentStockName = getCurrentStockName(stockCode, stockName);
		    StringBuilder sb = new StringBuilder();
		    return sb.append(stockCode).append(SymbolType.COMMA.getValue()).append(currentStockName).toString();
		}).collect(Collectors.toList());
	    }
	} catch (Exception e) {

	    logger.error(String.format("url [%s]", url), e);

	    if (reTryTimes > 1) {
		try {
		    Thread.sleep(new Random().nextInt(9) * 1000);
		} catch (InterruptedException e1) {
		    e1.printStackTrace();
		}
		return getStockLines(marketType, categoryName, reTryTimes - 1);
	    }
	}

	return new ArrayList<>();
    }

    /**
     * <pre>
     * pattern : stockCode, pageStockCode, year, q1, q2, q3, q4
     * </pre>
     * 
     * @param stockCode
     * @return
     * @throws IOException
     */
    @Override
    public List<String> getStockEPSLines(String stockCode) throws IOException {

	return this.getStockEPSLines(stockCode, RETRY_TIMES);
    }

    public List<String> getStockEPSLines(String stockCode, int reTryTimes) throws IOException {

	String urlTemplate = "http://histock.tw/stock/financial.aspx?no=%s&st=2";
	String url = String.format(urlTemplate, stockCode);

	try {
	    Document document = JsoupUtils.getDocument(url);
	    Elements trList = document.select(".tb-stock.text-center tr");

//	    logger.debug(trList.toString());
	    
	    if (CollectionUtils.isNotEmpty(trList)) {

		List<Element> yearTHList = trList.get(0).select("th").subList(1, trList.get(0).select("th").size());
		List<Element> q1TDList = trList.get(1).select("td");
		List<Element> q2TDList = trList.get(2).select("td");
		List<Element> q3TDList = trList.get(3).select("td");
		List<Element> q4TDList = trList.get(4).select("td");
		
		// validate if the parameter of stockCode is the parser html stockCode
		String pageStockCode = getHtmlStockCode(document.select("div.info-left.w160 > div ").get(0));
//		logger.debug("pageStockCode["+pageStockCode+"]");

		AtomicInteger count = new AtomicInteger();

		List<String> lines = new ArrayList<>();

		yearTHList.forEach(th -> {

		    int index = count.incrementAndGet() - 1;// start from 1
		    String year = th.text();
		    if (StringUtils.isNotBlank(year) && NumberUtils.isNumber(year)) {
			String q1 = q1TDList.get(index).text();
			String q2 = q2TDList.get(index).text();
			String q3 = q3TDList.get(index).text();
			String q4 = q4TDList.get(index).text();
			String line = String.join(",", stockCode, pageStockCode, year, q1, q2, q3, q4);
			lines.add(line);
		    }
		});

		return lines;
	    }
	} catch (Exception e) {

	    logger.error(String.format("url [%s]", url), e);

	    if (reTryTimes > 1) {
		try {
		    Thread.sleep(new Random().nextInt(9) * 1000);
		} catch (InterruptedException e1) {
		    e1.printStackTrace();
		}
		return getStockEPSLines(stockCode, reTryTimes - 1);
	    }
	}

	return new ArrayList<>();

    }

    /**
     * <pre>
     * pattern : stockCode, rocYear, cashDividend, stockDividend
     * </pre>
     */
    @Override
    public List<String> getStockDividendLines(String stockCode) throws IOException {

	return this.getStockDividendLines(stockCode, RETRY_TIMES);
    }

    public List<String> getStockDividendLines(String stockCode, int reTryTimes) throws IOException {

	String urlTemplate = "https://tw.stock.yahoo.com/d/s/dividend_%s.html";
	String url = String.format(urlTemplate, stockCode.substring(0, 4));

	try {
	    Document document = JsoupUtils.getDocument(url);
	    Element table = document.select("tr[bgcolor='#FEC003'] table").get(1);

	    if (table != null) {

		Elements trList = table.select("tr");

		return trList.subList(1, trList.size()).stream().map(tr -> {
		    Elements tdList = tr.select("td");
		    String rocYear = tdList.get(0).text();
		    String cashDividend = tdList.get(1).text();
		    String stockDividend = tdList.get(4).text();
		    return String.join(",", stockCode, rocYear, cashDividend, stockDividend);
		}).collect(Collectors.toList());

	    }
	} catch (Exception e) {
	    logger.error(String.format("url [%s]", url), e);

	    if (reTryTimes > 1) {
		try {
		    Thread.sleep(new Random().nextInt(9) * 1000);
		} catch (InterruptedException e1) {
		    e1.printStackTrace();
		}
		return getStockDividendLines(stockCode, reTryTimes - 1);
	    }
	}

	return new ArrayList<>();
    }

    /*
     * patch stockName from HiStock website
     * 
     */
    private String getCurrentStockName(String stockCode, String stockName) {

	if (StringUtils.isNotBlank(stockName)) {
	    // remove 'KY' '-' stock symbol
	    String _stockName = stockName.replaceAll("[a-zA-Z-]", "");

	    if (!CharUtils.isChineseSection(_stockName)) {
		String patchStockName = null;
		try {
		    patchStockName = getStockNameByHiStock(stockCode);
		    logger.debug("stockCode {} , stockName {} ,patch {} ", stockCode, stockName, patchStockName);
		} catch (IOException e) {
		    throw new RuntimeException(e);
		}

		return patchStockName;
	    }
	}
	return stockName;
    }

    public String getStockNameByHiStock(String stockCode) throws IOException {

	String urlTemplate = "https://histock.tw/stock/%s";
	String url = String.format(urlTemplate, stockCode);
	Document document = JsoupUtils.getDocument(url);
//	logger.debug(document.toString());
	Elements h3 = document.select("div.info-left.w600 > div h3 ");
	return h3.text();
    }

    private String getHtmlStockCode(Element tr) {

	String text = tr.select("span").text();
	int start = StringUtils.indexOf(text, "(");
	int end = StringUtils.indexOf(text, ")");
	String pageStockCode = StringUtils.substring(text, start + 1, end);
	return pageStockCode;
    }

}
