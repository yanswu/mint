package idv.mint.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public abstract class AbstractDao<PK extends Serializable, T> {

    private Class<T> entityClass;

    @PersistenceContext
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public AbstractDao() {
	this.entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    protected EntityManager getEntityManager() {
	return this.entityManager;
    }

    protected T getByKey(PK key) {
	return (T) entityManager.find(entityClass, key);
    }

    protected void persist(T entity) {
	entityManager.persist(entity);
    }

    protected void update(T entity) {
	entityManager.merge(entity);
    }

    protected void delete(T entity) {
	entityManager.remove(entity);
    }
}
