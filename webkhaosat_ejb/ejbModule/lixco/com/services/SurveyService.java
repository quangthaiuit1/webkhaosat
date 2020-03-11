package lixco.com.services;

import java.util.ArrayList;
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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lixco.com.entities.Survey;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class SurveyService extends AbstractService<Survey>{
	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext ct;

	@Override
	protected Class<Survey> getEntityClass() {
		return Survey.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
	
	public List<Survey> find(String employeeCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Survey> cq = cb.createQuery(Survey.class);
		Root<Survey> root = cq.from(Survey.class);
		List<Predicate> queries = new ArrayList<>();
		Predicate deleteQuery = cb.equal(root.get("isDeleted"), false);
		queries.add(deleteQuery);
		if (employeeCode != null) {
			employeeCode = "%" + employeeCode + "%";
			Predicate employeeCodeQuery = cb.like(root.get("usersJson"), employeeCode);
			queries.add(employeeCodeQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<Survey> query = em.createQuery(cq);
		query.getResultList();
		return query.getResultList();
	}
}
