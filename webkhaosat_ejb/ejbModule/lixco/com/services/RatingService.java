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

import lixco.com.entities.Rating;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class RatingService extends AbstractService<Rating>{

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext ct;

	@Override
	protected Class<Rating> getEntityClass() {
		return Rating.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
	
	public List<Rating> find(Long questionId, Long surveyId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Rating> cq = cb.createQuery(Rating.class);
		Root<Rating> root = cq.from(Rating.class);
		List<Predicate> queries = new ArrayList<>();
		Predicate deleteQuery = cb.equal(root.get("isDeleted"), false);
		queries.add(deleteQuery);
		if (questionId != 0) {
			Predicate answerTypeQuery = cb.equal(root.get("question").get("id"), questionId);
			queries.add(answerTypeQuery);
		}
		if(surveyId != 0) {
			Predicate surveyIdQuery = cb.equal(root.get("question").get("survey").get("id"), surveyId);
			queries.add(surveyIdQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<Rating> query = em.createQuery(cq);
		return query.getResultList();
	}

}
