package idv.mint.web.controller;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import idv.mint.bean.StockCategory;
import idv.mint.bean.StockSheet;
import idv.mint.entity.enums.StockMarketType;
import idv.mint.service.CrawlerService;
import idv.mint.type.CrawlType;

@RestController
@RequestMapping(value="/crawl/stock")
public class CrawlStockController {

    private static final Logger logger = LogManager.getLogger(CrawlStockController.class);

    @Autowired
    CrawlerService crawlService;
    
    @RequestMapping(value = { "/category/{marketType}" }, method = RequestMethod.GET)
    public ResponseEntity<List<StockCategory>> getStockCategory(@PathVariable String marketType) {

	try {
	    if (StringUtils.isNotBlank(marketType) && StringUtils.isNumeric(marketType)) {
		StockMarketType stockMarketType = StockMarketType.find(Integer.parseInt(marketType));
		if (!stockMarketType.isUnknown()) {
		    List<StockCategory> categories = crawlService.getStockCategoryList(CrawlType.ONLINE,stockMarketType);
		    return new ResponseEntity<List<StockCategory>>(categories,HttpStatus.OK);
		}
	    }
	} catch (IOException e) {
	    logger.error(e, e);
	}
	return new ResponseEntity<List<StockCategory>>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = { "/allCategories" }, method = RequestMethod.GET)
    public ResponseEntity<List<StockCategory>> getAllStockCategories() {
	
	try {
	    List<StockCategory> categories = crawlService.getAllStockCategories(CrawlType.ONLINE) ;
	    return new ResponseEntity<List<StockCategory>>(categories,HttpStatus.OK);
	} catch (IOException e) {
	    logger.error(e, e);
	}
	return new ResponseEntity<List<StockCategory>>(HttpStatus.NOT_FOUND);
    }
    

    @RequestMapping(value = { "/sheet/{stockCode}" }, method = RequestMethod.GET)
    public ResponseEntity<List<StockSheet>> getStockSheet(@PathVariable String stockCode){
	
	try {
	    List<StockSheet> stockSheetList = crawlService.getStockSheetList(CrawlType.FILE,stockCode);
	    if(!CollectionUtils.isEmpty(stockSheetList)) {
		return new ResponseEntity<List<StockSheet>>(stockSheetList,HttpStatus.OK);
	    }
	} catch (Exception e) {
	    logger.error(e, e);
	}
	return new ResponseEntity<List<StockSheet>>(HttpStatus.NOT_FOUND);
    }

}
