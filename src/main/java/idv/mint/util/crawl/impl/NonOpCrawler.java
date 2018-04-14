package idv.mint.util.crawl.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import idv.mint.entity.enums.StockMarketType;
import idv.mint.util.crawl.Crawler;

public class NonOpCrawler implements Crawler {

    @Override
    public List<String> getStockCategoryLines(StockMarketType marketType)  {
	return new ArrayList<>();
    }

    @Override
    public List<String> getStockEPSLines(String stockCode)  {
	return new ArrayList<>();
    }

    @Override
    public List<String> getStockDividendLines(String stockCode) {
	return new ArrayList<>();
    }

    @Override
    public List<String> getStockLines(StockMarketType marketType) throws IOException {
	return new ArrayList<>();
    }

    @Override
    public List<String> getStockLines(StockMarketType marketType, String categoryName) {
	return new ArrayList<>();
    }

    @Override
    public List<String> getStockRoeNetIncomeLines(String stockCode) {
	return new ArrayList<>();
    }


}
