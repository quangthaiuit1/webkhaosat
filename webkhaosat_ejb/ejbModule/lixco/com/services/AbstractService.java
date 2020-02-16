package lixco.com.services;

import java.util.List;

import javax.ejb.SessionContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;



public abstract	class AbstractService<T> implements IAbstractService<T> {
	
	protected abstract Class<T> getEntityClass();
	
	protected abstract EntityManager getEntityManager();
	
	protected abstract SessionContext getUt();
	
	@Override
	public T create(T entity){
		getEntityManager().persist(entity);
		return getEntityManager().merge(entity);
	}
	
	@Override
	public T update(T entity){
		return getEntityManager().merge(entity);

	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean delete(T entity) {
		boolean result = true;
		try {
			getEntityManager().remove(getEntityManager().merge(entity));
			getEntityManager().flush();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
			getUt().setRollbackOnly();
		}
		return result;
	}

	@Override
	public T findById(long id) {
		try{
		return getEntityManager().find(getEntityClass(), id);
		}catch(Exception e){
			e.printStackTrace();return null;
		}
	}

	@Override
	public List<T> findAll() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
		Root<T> root = cq.from(getEntityClass());
		cq.select(root);
		TypedQuery<T> query = getEntityManager().createQuery(cq);

		return query.getResultList();
	}	
	
	@Override
	public List<T> findAllByFilter(){
		try {
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
			Root<T> root = cq.from(getEntityClass());
			Predicate predicateForIsDeleted
			  = cb.equal(root.get("isDeleted"), false);
			cq.where(predicateForIsDeleted);
			TypedQuery<T> query = getEntityManager().createQuery(cq);
			List<T> t = query.getResultList();
			return t;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

