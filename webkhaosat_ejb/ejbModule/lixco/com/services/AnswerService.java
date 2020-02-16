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

import lixco.com.entities.Answer;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class AnswerService extends AbstractService<Answer>{

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext ct;

	@Override
	protected Class<Answer> getEntityClass() {
		return Answer.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
	
	public List<Answer> find(Long questionId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Answer> cq = cb.createQuery(Answer.class);
		Root<Answer> root = cq.from(Answer.class);
		List<Predicate> queries = new ArrayList<>();
		Predicate deleteQuery = cb.equal(root.get("isDeleted"), false);
		queries.add(deleteQuery);
		if (questionId != null) {
			Predicate answerTypeQuery = cb.equal(root.get("question").get("id"), questionId);
			queries.add(answerTypeQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<Answer> query = em.createQuery(cq);
		return query.getResultList();
	}

}
