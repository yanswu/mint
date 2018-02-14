package idv.mint.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;

public abstract class AbstractDao<PK extends Serializable, T> {

    private Class<T> entityClass;

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public AbstractDao() {
	this.entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public EntityManager getEntityManager() {
	return this.entityManager;
    }

    public T getByKey(PK key) {
	return (T) entityManager.find(entityClass, key);
    }
    
    public CriteriaBuilder getCriteriaBuilder() {
	
	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	return cb;
    }

    public void persistList(List<T> entityList) {
	entityList.forEach(entity->{
	    persist(entity);
	});
    }
    
    public void persist(T entity) {
	entityManager.persist(entity);
    }

    public void update(T entity) {
	entityManager.merge(entity);
    }

    public void delete(T entity) {
	entityManager.remove(entity);
    }
    
    
}
