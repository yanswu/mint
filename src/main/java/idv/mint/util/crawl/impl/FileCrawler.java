package idv.mint.util.crawl.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import idv.mint.context.enums.SymbolType;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.support.PathSettings;
import idv.mint.util.crawl.Crawler;

public class FileCrawler implements Crawler {

    @Override
    public List<String> getStockCategoryLines(StockMarketType marketType) throws IOException {

	Path path = null;

	if (marketType == null || marketType.isUnknown()) {
	    return new ArrayList<>();
	}

	if (marketType.isTSE()) {
	    path = PathSettings.STOCK_CATEGORY_TSE_CSV.getPath();
	} else if (marketType.isOTC()) {
	    path = PathSettings.STOCK_CATEGORY_OTC_CSV.getPath();
	}

	if (path != null) {
	    return Files.readAllLines(path);
	}
	return new ArrayList<>();
    }

    @Override
    public List<String> getStockEPSLines(String stockCode) {

	try {

	    StockMarketType stockMarketType = getStockMarketType(stockCode);

	    Path readPath = null;

	    if (stockMarketType.isTSE()) {
		readPath = PathSettings.STOCK_EPS_TSE_CSV.getPath();
	    } else if (stockMarketType.isOTC()) {
		readPath = PathSettings.STOCK_EPS_OTC_CSV.getPath();
	    }

	    if (readPath != null) {

		List<String> lines = Files.readAllLines(readPath);
		return lines.stream().filter(line -> {
		    return StringUtils.equals(stockCode, StringUtils.split(line, ",")[0]);
		}).collect(Collectors.toList());
	    }

	    return new ArrayList<>();

	} catch (IOException e) {
	    throw new RuntimeException(e);
	}

    }

    @Override
    public List<String> getStockDividendLines(String stockCode) {

	try {

	    StockMarketType stockMarketType = getStockMarketType(stockCode);

	    Path readPath = null;

	    if (stockMarketType.isTSE()) {
		readPath = PathSettings.STOCK_DIVIDEND_TSE_CSV.getPath();
	    } else if (stockMarketType.isOTC()) {
		readPath = PathSettings.STOCK_DIVIDEND_OTC_CSV.getPath();
	    }

	    if (readPath != null) {

		String comma = SymbolType.COMMA.getValue();

		List<String> lines = Files.readAllLines(readPath);

		return lines.stream().filter(line -> {
		    return StringUtils.equals(stockCode, StringUtils.split(line, comma)[0]);
		}).collect(Collectors.toList());

	    }

	} catch (Exception e) {

	    throw new RuntimeException(e);
	}

	return new ArrayList<>();
    }

    @Override
    public List<String> getStockLines(StockMarketType marketType) {

	if (marketType == null || marketType.isUnknown()) {
	    return new ArrayList<>();
	}

	try {

	    Path path = null;

	    if (marketType.isTSE()) {
		path = PathSettings.STOCK_TSE_CSV.getPath();
	    } else if (marketType.isOTC()) {
		path = PathSettings.STOCK_OTC_CSV.getPath();
	    }

	    return Files.readAllLines(path);

	} catch (Exception e) {

	    throw new RuntimeException(e);
	}

    }

    @Override
    public List<String> getStockLines(StockMarketType marketType, String categoryName) {

	List<String> lines = getStockLines(marketType);

	String comma = SymbolType.COMMA.getValue();

	return lines.stream().filter(line -> {
	    String[] sections = StringUtils.split(line, comma);
	    return StringUtils.equals(categoryName, sections[2]);
	}).collect(Collectors.toList());

    }

    private StockMarketType getStockMarketType(String stockCode) throws IOException {

	boolean isContainStockCodePath = isPathContainStockCode(PathSettings.STOCK_TSE_CSV.getPath(), stockCode);

	if (isContainStockCodePath) {
	    return StockMarketType.TSE;
	} else {
	    isContainStockCodePath = isPathContainStockCode(PathSettings.STOCK_OTC_CSV.getPath(), stockCode);

	    return isContainStockCodePath ? StockMarketType.OTC : StockMarketType.UNKNOWN;
	}

    }

    private boolean isPathContainStockCode(Path readPath, String stockCode) throws IOException {

	List<String> lines = Files.readAllLines(readPath);
	long counts = lines.stream().filter(line -> {
	    return StringUtils.split(line, ",")[3].equals(stockCode);
	}).count();
	return counts > 0;
    }

    @Override
    public List<String> getIncomeStatementLines(String stockCode) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<String> getBalanceSheetLines(String stockCode) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public List<String> getStockPriceHistoryLines(String stockCode) {
	// TODO Auto-generated method stub
	return null;
    }
}
