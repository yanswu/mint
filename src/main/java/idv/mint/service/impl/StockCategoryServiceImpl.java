package idv.mint.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idv.mint.bean.StockCategory;
import idv.mint.dao.StockCategoryDao;
import idv.mint.entity.StockCategoryEntity;
import idv.mint.service.StockCategoryService;

@Service("stockCategoryService")
public class StockCategoryServiceImpl implements StockCategoryService {
    
    @Autowired
    private StockCategoryDao stockCategoryDao;
    
    @Override
    @Transactional
    public void saveStockCategoryEntities(List<StockCategory> list) {
	
	if(CollectionUtils.isNotEmpty(list)) {
	    
//	    AtomicInteger index = new AtomicInteger();
//	    index.incrementAndGet();
	    List<StockCategoryEntity> entityList = list.stream().map(category->{
		
		Integer marketTypeValue = category.getMarketType().getValue();
		Integer orderNo = category.getOrderNo();

		StockCategoryEntity entity = new StockCategoryEntity();

		entity.setStockCategoryId(String.valueOf(marketTypeValue)+String.valueOf(orderNo));
		entity.setMarketType(marketTypeValue);
		entity.setCategoryName(category.getName());
		entity.setOrderNo(orderNo);
		
		return entity;
		
	    }).collect(Collectors.toList());
	    
	    entityList.stream().forEach(entity->{
		
		stockCategoryDao.persist(entity);		
	    });

	}
	    
	
    }

}
