package lixco.com.services;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lixco.com.entities.User;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class UserService extends AbstractService<User>{

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext ct;

	@Override
	protected Class<User> getEntityClass() {
		return User.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
	
//	public List<Rating> find(Long questionId) {
//
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<Rating> cq = cb.createQuery(Rating.class);
//		Root<Rating> root = cq.from(Rating.class);
//		List<Predicate> queries = new ArrayList<>();
//		Predicate deleteQuery = cb.equal(root.get("isDeleted"), false);
//		queries.add(deleteQuery);
//		if (questionId != null) {
//			Predicate answerTypeQuery = cb.equal(root.get("question").get("id"), questionId);
//			queries.add(answerTypeQuery);
//		}
//		Predicate data[] = new Predicate[queries.size()];
//		for (int i = 0; i < queries.size(); i++) {
//			data[i] = queries.get(i);
//		}
//		Predicate finalPredicate = cb.and(data);
//		cq.where(finalPredicate);
//		TypedQuery<Rating> query = em.createQuery(cq);
//		return query.getResultList();
//	}

}
