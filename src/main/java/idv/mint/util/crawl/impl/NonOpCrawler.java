package idv.mint.util.crawl.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import idv.mint.entity.enums.StockMarketType;
import idv.mint.util.crawl.Crawler;

public class NonOpCrawler implements Crawler {

    @Override
    public List<String> getStockCategory(StockMarketType marketType) throws IOException {
	return new ArrayList<>();
    }

    @Override
    public List<String> getStockEPS(String stockCode) throws IOException {
	return new ArrayList<>();
    }

    @Override
    public List<String> getStockDividend(String stockCode) throws IOException {
	return new ArrayList<>();
    }

    @Override
    public List<String> getStock(StockMarketType marketType, String categoryName) throws IOException {
	// TODO Auto-generated method stub
	return null;
    }

}
