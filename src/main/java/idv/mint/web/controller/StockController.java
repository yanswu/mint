package idv.mint.web.controller;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import idv.mint.bean.Stock;
import idv.mint.service.StockService;

@RestController
@RequestMapping(value = "/stock")
public class StockController {

    private static final Logger logger = LogManager.getLogger(StockController.class);

    @Autowired
    StockService stockService;

    @RequestMapping(value = { "/{stockCode}" }, method = RequestMethod.GET)
    public ResponseEntity<Stock> getStock(@PathVariable String stockCode) {

	logger.debug("stockCode is[" + stockCode + "]");

	Stock stock = stockService.getStock(stockCode);

	if (stock != null) {
	    return new ResponseEntity<Stock>(stock, HttpStatus.OK);
	} else {
	    return new ResponseEntity<Stock>(HttpStatus.NOT_FOUND);
	}
    }

    @RequestMapping(value = { "/lastestEps/{stockCode}" }, method = RequestMethod.PUT)
    public ResponseEntity<Stock> updateLastEep(@PathVariable String stockCode) {

	logger.debug("stockCode is[" + stockCode + "]");

	Stock stock = stockService.getStock(stockCode);

	if (stock != null) {

	    try {
		stockService.updateLastestEPS(stockCode);
		stock = stockService.getStock(stockCode);
	    } catch (IOException e) {
		logger.error(e, e);
	    }
	    return new ResponseEntity<Stock>(stock, HttpStatus.OK);

	} else {
	    return new ResponseEntity<Stock>(HttpStatus.NOT_FOUND);
	}
    }

    @RequestMapping(value = { "/lastestDividend/{stockCode}" }, method = RequestMethod.PUT)
    public ResponseEntity<Stock> updateLastestDividend(@PathVariable String stockCode) {
	
	logger.debug("stockCode is[" + stockCode + "]");
	
	Stock stock = stockService.getStock(stockCode);
	
	if (stock != null) {
	    
	    try {
		stockService.updateLastestDividend(stockCode);
		stock = stockService.getStock(stockCode);
	    } catch (IOException e) {
		logger.error(e, e);
	    }
	    return new ResponseEntity<Stock>(stock, HttpStatus.OK);
	    
	} else {
	    return new ResponseEntity<Stock>(HttpStatus.NOT_FOUND);
	}
    }
}
