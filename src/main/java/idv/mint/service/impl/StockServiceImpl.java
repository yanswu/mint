package idv.mint.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idv.mint.bean.Stock;
import idv.mint.bean.StockCategory;
import idv.mint.bean.StockSheet;
import idv.mint.dao.StockCategoryDao;
import idv.mint.dao.StockDao;
import idv.mint.dao.StockSheetDao;
import idv.mint.entity.StockCategoryEntity;
import idv.mint.entity.StockEntity;
import idv.mint.entity.StockSheetEntity;
import idv.mint.entity.enums.OverseasType;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.entity.pk.StockSheetPk;
import idv.mint.service.CrawlerService;
import idv.mint.service.StockService;
import idv.mint.util.crawl.Crawler;
import idv.mint.util.stock.StockConverter;
import idv.mint.util.stock.StockUtils;

@Service("stockService")
public class StockServiceImpl implements StockService {

    private static final Logger logger = LogManager.getLogger(StockServiceImpl.class);

    @Autowired
    private StockDao stockDao;

    @Autowired
    private StockCategoryDao stockCategoryDao;

    @Autowired
    private StockSheetDao stockSheetDao;

    @Autowired
    private CrawlerService crawlerService;

    @Transactional
    @Override
    public void saveStockEntities(List<Stock> stockList) {

	if (CollectionUtils.isNotEmpty(stockList)) {

	    stockList.stream().forEach(stock -> {

		String stockName = stock.getStockName();

		StockEntity entity = new StockEntity();
		entity.setStockId(stock.getStockCode());
		entity.setStockName(stockName);
		entity.setStockCategoryId(stock.getStockCategory().getStockCategoryId());
		entity.setOverseasType(stockName.contains(OverseasType.KY.toString()) ? OverseasType.KY.getValue() : OverseasType.LOCAL.getValue());

		stockDao.persist(entity);
	    });
	}
    }

    @Transactional
    public void saveStockSheet(StockSheet stockSheet) {

	this.saveStockSheetEntities(Arrays.asList(stockSheet));
    }

    @Transactional
    @Override
    public void saveStockSheetEntities(List<StockSheet> stockSheetList) {

		if (CollectionUtils.isNotEmpty(stockSheetList)) {

			LocalDateTime now = LocalDateTime.now();

			stockSheetList.stream()
					.forEach(stockSheet -> {

						LocalDate baseDate = stockSheet.getBaseDate();
						String stockCode = stockSheet.getStockCode();

						StockSheetPk stockSheetPk = new StockSheetPk(stockCode, baseDate);

						StockSheetEntity entity = new StockSheetEntity();
						entity.setSheetPk(stockSheetPk);
						entity.setEpsQ1(stockSheet.getEpsQ1());
						entity.setEpsQ2(stockSheet.getEpsQ2());
						entity.setEpsQ3(stockSheet.getEpsQ3());
						entity.setEpsQ4(stockSheet.getEpsQ4());
						entity.setStockDividend(stockSheet.getStockDividend());
						entity.setCashDividend(stockSheet.getCashDividend());
						entity.setNetIncome(stockSheet.getNetIncome());
						entity.setLongTermInvest(stockSheet.getLongTermInvest());
						entity.setFixedAsset(stockSheet.getFixedAsset());
						entity.setShareholderEquity(stockSheet.getShareholderEquity());
						entity.setInsertTime(now);
						stockSheetDao.persist(entity);
					});
		}
	}

    @Transactional
    @Override
    public void saveStockCategoryEntities(List<StockCategory> list) {

		if (CollectionUtils.isNotEmpty(list)) {

			// AtomicInteger index = new AtomicInteger();
			// index.incrementAndGet();
			List<StockCategoryEntity> entityList = list.stream()
					.map(category -> {

						StockCategoryEntity entity = new StockCategoryEntity();

						entity.setMarketType(category.getMarketType().getValue());
						entity.setOrderNo(category.getOrderNo());
						entity.setCategoryName(category.getName());
						entity.setStockCategoryId(category.getStockCategoryId());

						return entity;

					})
					.collect(Collectors.toList());

			entityList.stream()
					.forEach(entity -> {
						stockCategoryDao.persist(entity);
					});
		}
	}

    @Override
	public Stock getStock(String stockCode) {

		if (StringUtils.isBlank(stockCode)) {
			return null;
		}

		StockEntity stockEntity = stockDao.getByKey(stockCode);

		if (stockEntity == null) {
			return null;
		}

		// 1. create stock
		Stock stock = new Stock(stockEntity.getStockCode(),stockEntity.getStockName());

		// 2. create stockCategory
		String stockCategoryId = stockEntity.getStockCategoryId();
		StockCategoryEntity stockCategoryEntity = stockCategoryDao.getByKey(stockCategoryId);
		
		StockCategory stockCategory = new StockCategory();

		if (stockCategoryEntity != null) {
			stockCategory.setMarketType(StockMarketType.find(stockCategoryEntity.getMarketType()));
			stockCategory.setOrderNo(stockCategoryEntity.getOrderNo());
			stockCategory.setName(stockCategoryEntity.getCategoryName());
			stockCategory.setStockCategoryId(stockCategoryId);
		}

		stock.setStockCategory(stockCategory);

		// 3. create stockSheetList
		List<StockSheetEntity> stockSheetEntityList = stockSheetDao.findByStockId(stockCode);

		List<StockSheet> stockSheetList = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(stockSheetEntityList)) {

			stockSheetList = stockSheetEntityList.stream()
					.map(entity -> {

						StockSheet stockSheet = new StockSheet();
						stockSheet.setBaseDate(entity.getSheetPk().getCalYear());

						BigDecimal epsQ1 = entity.getEpsQ1();
						BigDecimal epsQ2 = entity.getEpsQ2();
						BigDecimal epsQ3 = entity.getEpsQ3();
						BigDecimal epsQ4 = entity.getEpsQ4();
						
						BigDecimal totalEps = Arrays.asList(epsQ1, epsQ2, epsQ3, epsQ4)
								.stream()
								.filter(Objects::nonNull)
								.reduce(BigDecimal.ZERO, BigDecimal::add);

						stockSheet.setEpsQ1(epsQ1);
						stockSheet.setEpsQ2(epsQ2);
						stockSheet.setEpsQ3(epsQ3);
						stockSheet.setEpsQ4(epsQ4);
						stockSheet.setTotalEps(totalEps);

						stockSheet.setCashDividend(entity.getCashDividend().setScale(2, RoundingMode.DOWN));
						stockSheet.setStockDividend(entity.getStockDividend().setScale(2, RoundingMode.DOWN));
						stockSheet.setLongTermInvest(entity.getLongTermInvest());
						stockSheet.setShareholderEquity(entity.getShareholderEquity());
						stockSheet.setNetIncome(entity.getNetIncome());
						stockSheet.setFixedAsset(entity.getFixedAsset());

						return stockSheet;

					})
					.collect(Collectors.toList());
		}

		// 計算還原股價
		BigDecimal stockPrice = crawlerService.getStockPrice(stockCode);
		StockUtils.calculateValuePrice(stockPrice, stockSheetList);
		stock.setStockSheetList(stockSheetList);

		return stock;
	}

    @Transactional
    @Override
    public void updateLastestEPS(String stockCode) throws IOException {

	Crawler crawler = Crawler.createWebCrawler();
	List<String> epsLines = crawler.getStockEPSLines(stockCode);
	String lastLine = epsLines.get(epsLines.size() - 1);

	StockSheet stockSheet = StockConverter.createStockSheetEps(lastLine);

	StockSheetEntity entity = stockSheetDao.findByPk(stockSheet.getStockCode(), stockSheet.getBaseDate());

		if (entity == null) {
			stockSheetDao.persist(entity);
		} else {
			stockSheetDao.updateLastestEPS(stockSheet);
		}
	}

    @Transactional
    @Override
    public void updateLastestDividend(String stockCode) throws IOException {

		Crawler crawler = Crawler.createWebCrawler();

		List<String> dividendLines = crawler.getStockDividendLines(stockCode);

		String lastLine = dividendLines.get(0);

		StockSheet stockSheet = StockConverter.createStockSheetDividend(lastLine);

		StockSheetEntity entity = stockSheetDao.findByPk(stockSheet.getStockCode(), stockSheet.getBaseDate());

		if (entity == null) {
			this.saveStockSheet(stockSheet);
		} else {
			stockSheetDao.updateLastestDividend(stockSheet);
		}
	}

}
