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

import lixco.com.entities.Question;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class QuestionService extends AbstractService<Question>{

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext ct;

	@Override
	protected Class<Question> getEntityClass() {
		return Question.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
	
	public List<Question> find(Long questionTypeId,Long setofquestionsId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Question> cq = cb.createQuery(Question.class);
		Root<Question> root = cq.from(Question.class);
		List<Predicate> queries = new ArrayList<>();
		Predicate deleteQuery = cb.equal(root.get("isDeleted"), false);
		queries.add(deleteQuery);
		if (questionTypeId != null) {
			Predicate questionTypeQuery = cb.equal(root.get("questiontype").get("id"), questionTypeId);
			queries.add(questionTypeQuery);
		}
		if(setofquestionsId != null) {
			Predicate setofquestionIdQuery = cb.equal(root.get("setofquestions").get("id"), setofquestionsId);
			queries.add(setofquestionIdQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<Question> query = em.createQuery(cq);
		System.out.println("ahihi");
		query.getResultList();
		return query.getResultList();
	}

}
