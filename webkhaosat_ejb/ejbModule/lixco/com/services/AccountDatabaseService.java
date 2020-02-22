package lixco.com.services;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import lixco.com.entities.AccountDatabase;


@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class AccountDatabaseService extends AbstractService<AccountDatabase> {
	
	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext ct;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	@Override
	protected Class<AccountDatabase> getEntityClass() {
		return AccountDatabase.class;
	}

	public AccountDatabase findByName(String name) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<AccountDatabase> cq = cb.createQuery(getEntityClass());
		Root<AccountDatabase> root = cq.from(getEntityClass());
		cq.select(root).where(cb.equal(root.get("name"), name));
		TypedQuery<AccountDatabase> query = getEntityManager().createQuery(cq);
		List<AccountDatabase> results = query.getResultList();
		if (results.size() != 0) {
			return results.get(0);
		} else {
			return null;
		}
	}

}
