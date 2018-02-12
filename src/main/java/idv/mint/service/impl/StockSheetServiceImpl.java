package idv.mint.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idv.mint.bean.StockSheet;
import idv.mint.dao.StockSheetDao;
import idv.mint.entity.StockSheetEntity;
import idv.mint.entity.StockSheetPk;
import idv.mint.service.StockSheetService;


@Service("stockSheetService")
public class StockSheetServiceImpl implements StockSheetService {
    
    @Autowired
    private StockSheetDao stockSheetDao;
    
    @Transactional
    @Override
    public void saveStockSheetEntities(List<StockSheet> stockSheetList) {
	
	if(CollectionUtils.isNotEmpty(stockSheetList)) {
	    
	    stockSheetList.stream().forEach(stockSheet->{
		
		LocalDate baseDate = stockSheet.getBaseDate();
		String stockCode = stockSheet.getStockCode();
		
		StockSheetPk stockSheetPk = new StockSheetPk(stockCode,baseDate);
		
		StockSheetEntity entity = new StockSheetEntity();
		entity.setSheetPk(stockSheetPk);
		entity.setEpsQ1(stockSheet.getEpsQ1());
		entity.setEpsQ2(stockSheet.getEpsQ2());
		entity.setEpsQ3(stockSheet.getEpsQ3());
		entity.setEpsQ4(stockSheet.getEpsQ4());
		entity.setStockDividend(stockSheet.getStockDividend());
		entity.setCashDividend(stockSheet.getCashDividend());
		stockSheetDao.persist(entity);
	    });
	}
    }

}
