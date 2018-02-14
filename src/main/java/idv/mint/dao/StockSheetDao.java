package idv.mint.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import idv.mint.entity.StockSheetEntity;
import idv.mint.entity.StockSheetPk;

@Repository("stockSheetDao")
public class StockSheetDao extends AbstractDao<StockSheetPk, StockSheetEntity> {
    
    public List<StockSheetEntity> findByStockId(String stockId){
	
	String jpql = "select c from StockSheetEntity as c where c.sheetPk.stockId = : stockId";
	
	return this.getEntityManager().createQuery(jpql,StockSheetEntity.class).setParameter("stockId", stockId).getResultList();
	
    }
    
}
