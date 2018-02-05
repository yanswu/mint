package idv.mint.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idv.mint.bean.Stock;
import idv.mint.dao.StockDao;
import idv.mint.entity.StockEntity;
import idv.mint.entity.enums.OverseasType;
import idv.mint.service.StockService;

@Service("stockService")
public class StockServiceImpl implements StockService{
    
    @Autowired
    private StockDao stockDao;
    
    @Transactional
    @Override
    public void saveStockEntities(List<Stock> stockList) {
	
	if(CollectionUtils.isNotEmpty(stockList)) {
	    
	    stockList.stream().forEach(stock->{
		
		String stockName = stock.getStockName();
		
		StockEntity entity = new StockEntity();
		
		entity.setStockId(stock.getStockCode());
		entity.setStockName(stockName);
		entity.setStockCategoryId(stock.getStockCategory().getStockCategoryId());
		entity.setOverseasType(stockName.contains(OverseasType.KY.toString()) ? OverseasType.KY.getValue(): OverseasType.LOCAL.getValue());
		
		stockDao.persist(entity);
	    });
	}
    }
    

}
