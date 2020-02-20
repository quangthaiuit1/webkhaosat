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

import lixco.com.entities.User_Result;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class UserResultService extends AbstractService<User_Result>{

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext ct;

	@Override
	protected Class<User_Result> getEntityClass() {
		return User_Result.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
	public List<User_Result> find(Long setOfQuestionId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User_Result> cq = cb.createQuery(User_Result.class);
		Root<User_Result> root = cq.from(User_Result.class);
		List<Predicate> queries = new ArrayList<>();
		Predicate deleteQuery = cb.equal(root.get("isDeleted"), false);
		queries.add(deleteQuery);
		if (setOfQuestionId != null) {
			Predicate answerTypeQuery = cb.equal(root.get("question").get("setofquestions").get("id"), setOfQuestionId);
			queries.add(answerTypeQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<User_Result> query = em.createQuery(cq);
		return query.getResultList();
	}
}