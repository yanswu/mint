package idv.mint.util.crawl.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import idv.mint.entity.enums.StockMarketType;
import idv.mint.support.PathSettings;
import idv.mint.util.crawl.Crawler;
import idv.mint.util.stock.StockCreator;

public class FileCrawler implements Crawler {

    @Override
    public List<String> getStockCategory(StockMarketType marketType) throws IOException {

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
    public List<String> getStockEPS(String stockCode) throws IOException {

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
    }

    @Override
    public List<String> getStockDividend(String stockCode) throws IOException {

	StockMarketType stockMarketType = getStockMarketType(stockCode);

	Path readPath = null;

	if (stockMarketType.isTSE()) {
	    readPath = PathSettings.STOCK_DIVIDEND_TSE_CSV.getPath();
	} else if (stockMarketType.isOTC()) {
	    readPath = PathSettings.STOCK_DIVIDEND_OTC_CSV.getPath();
	}

	if (readPath != null) {

	    List<String> lines = Files.readAllLines(readPath);
	    return lines.stream().filter(line -> {
		return StringUtils.equals(stockCode, StringUtils.split(line, ",")[0]);
	    }).collect(Collectors.toList());
	}

	return new ArrayList<>();
    }

    @Override
    public List<String> getStock(StockMarketType marketType, String categoryName) throws IOException {

	if (marketType == null || marketType.isUnknown()) {
	    return new ArrayList<>();
	}

	Path path = null;

	if (marketType.isTSE()) {
	    path = PathSettings.STOCK_TSE_CSV.getPath();
	} else if (marketType.isOTC()) {
	    path = PathSettings.STOCK_OTC_CSV.getPath();
	}

	List<String> lines = Files.readAllLines(path);
	return lines.stream().filter(line ->{
	    String[] sections = StringUtils.split(line,",");
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
}
