package idv.mint.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idv.mint.bean.StockCategory;
import idv.mint.dao.StockCategoryDao;
import idv.mint.entity.StockCategoryEntity;
import idv.mint.service.StockCategoryService;

@Service("stockCategoryService")
public class StockCategoryServiceImpl implements StockCategoryService {
    
    private static final Logger logger = LogManager.getLogger(StockCategoryServiceImpl.class);

    @Autowired
    private StockCategoryDao stockCategoryDao;

    @Override
    @Transactional
    public void saveStockCategoryEntities(List<StockCategory> list) {

	if (CollectionUtils.isNotEmpty(list)) {

	    // AtomicInteger index = new AtomicInteger();
	    // index.incrementAndGet();
	    List<StockCategoryEntity> entityList = list.stream().map(category -> {

		StockCategoryEntity entity = new StockCategoryEntity();

		entity.setMarketType(category.getMarketType().getValue());
		entity.setOrderNo(category.getOrderNo());
		entity.setCategoryName(category.getName());
		entity.setStockCategoryId(category.getStockCategoryId());

		return entity;

	    }).collect(Collectors.toList());

	    entityList.stream().forEach(entity -> {
		stockCategoryDao.persist(entity);
	    });

	}

    }

}
