package idv.mint.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import idv.mint.bean.StockSheet;
import idv.mint.entity.StockSheetEntity;
import idv.mint.entity.StockSheetPk;

@Repository("stockSheetDao")
public class StockSheetDao extends AbstractDao<StockSheetPk, StockSheetEntity> {

    /**
     * 
     * @param stockId
     * @return
     */
    public List<StockSheetEntity> findByStockId(String stockId) {

	String jpql = "select c from StockSheetEntity as c where c.sheetPk.stockId = :stockId order by c.sheetPk.calYear";

	TypedQuery<StockSheetEntity> query = this.getEntityManager().createQuery(jpql, StockSheetEntity.class);
	query.setParameter("stockId", stockId);
	return query.getResultList();

    }

    /**
     * 
     * @param stockId
     * @param baseDate
     * @return
     */
    public StockSheetEntity findByPk(String stockId, LocalDate baseDate) {

	StockSheetPk key = new StockSheetPk(stockId, baseDate);
	StockSheetEntity entity = this.getByKey(key);

	return entity;
    }



    /**
     * 
     * @param stockSheet
     * @return
     */
    public int updateLastestEPS(StockSheet stockSheet) {

	String stockCode = stockSheet.getStockCode();
	LocalDate baseDate = stockSheet.getBaseDate();

	StringBuilder jpql = new StringBuilder();
	jpql.append(" update StockSheetEntity c set c.epsQ1 = :epsQ1,");
	jpql.append(" c.epsQ2 = :epsQ2 , c.epsQ3 = :epsQ3 , c.epsQ4 = :epsQ4 ,c.updateTime = :updateTime");
	jpql.append(" where c.sheetPk.stockId = :stockId and c.sheetPk.calYear = :calYear ");

	Query query = this.getEntityManager().createQuery(jpql.toString());

	query.setParameter("epsQ1", stockSheet.getEpsQ1());
	query.setParameter("epsQ2", stockSheet.getEpsQ2());
	query.setParameter("epsQ3", stockSheet.getEpsQ3());
	query.setParameter("epsQ4", stockSheet.getEpsQ4());
	query.setParameter("updateTime", LocalDateTime.now());
	query.setParameter("stockId", stockCode);
	query.setParameter("calYear", baseDate);

	return query.executeUpdate();

    }

    /**
     * 
     * @param stockSheet
     * @return
     */
    public int updateLastestDividend(StockSheet stockSheet) {

	String stockCode = stockSheet.getStockCode();
	LocalDate baseDate = stockSheet.getBaseDate();

	StringBuilder jpql = new StringBuilder();
	jpql.append(" update StockSheetEntity c ");
	jpql.append(" set c.cashDividend = :cashDividend, c.stockDividend = :stockDividend ,c.updateTime =:updateTime ");
	jpql.append(" where c.sheetPk.stockId = :stockId and c.sheetPk.calYear = :calYear ");

	Query query = this.getEntityManager().createQuery(jpql.toString());

	query.setParameter("cashDividend", stockSheet.getCashDividend());
	query.setParameter("stockDividend", stockSheet.getStockDividend());
	query.setParameter("updateTime", LocalDateTime.now());
	query.setParameter("stockId", stockCode);
	query.setParameter("calYear", baseDate);

	return query.executeUpdate();

    }

}
