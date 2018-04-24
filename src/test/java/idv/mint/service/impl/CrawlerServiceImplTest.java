package idv.mint.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import idv.mint.bean.StockSheet;
import idv.mint.context.config.AppConfig;
import idv.mint.context.config.JpaConfig;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.service.CrawlerService;
import idv.mint.type.CrawlType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class,JpaConfig.class })
@WebAppConfiguration
public class CrawlerServiceImplTest {

    @Autowired
    private WebApplicationContext wac;
    
//    @Autowired
//    CrawlerService crawlerService;


    @Test
    public void testGetStockSheetList() throws Exception {
	
	CrawlerService crawlerService = wac.getBean(CrawlerService.class);
	
	List<StockSheet> stockSheetList = crawlerService.getStockSheetList( StockMarketType.TSE);

	stockSheetList.stream().forEach(e -> {

	    String stockCode = e.getStockCode();

	    if (stockCode.equals("1773")) {

		int year = e.getBaseDate().getYear();

		if (2015 == year) {
		    Assert.assertThat(e.getEpsQ1(), Matchers.comparesEqualTo(new BigDecimal("0.9")));
		    Assert.assertThat(e.getEpsQ2(), Matchers.comparesEqualTo(new BigDecimal("1.02")));
		    Assert.assertThat(e.getEpsQ3(), Matchers.comparesEqualTo(new BigDecimal("1.06")));
		    Assert.assertThat(e.getEpsQ4(), Matchers.comparesEqualTo(new BigDecimal("1.1")));
		    Assert.assertThat(e.getStockDividend(), Matchers.comparesEqualTo(BigDecimal.ZERO));
		    Assert.assertThat(e.getCashDividend(), Matchers.comparesEqualTo(new BigDecimal(3)));
		}
		if (2016 == year) {
		    Assert.assertThat(e.getEpsQ1(), Matchers.comparesEqualTo(new BigDecimal("1.12")));
		    Assert.assertThat(e.getEpsQ2(), Matchers.comparesEqualTo(new BigDecimal("1.35")));
		    Assert.assertThat(e.getEpsQ3(), Matchers.comparesEqualTo(new BigDecimal("1.33")));
		    Assert.assertThat(e.getEpsQ4(), Matchers.comparesEqualTo(new BigDecimal("1.43")));
		    Assert.assertThat(e.getStockDividend(), Matchers.comparesEqualTo(BigDecimal.ZERO));
		    Assert.assertThat(e.getCashDividend(), Matchers.comparesEqualTo(new BigDecimal(4)));
		}
	    }
	});

    }

}
