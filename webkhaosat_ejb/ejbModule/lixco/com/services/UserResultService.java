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
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lixco.com.entities.Statistical;
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
	public List<User_Result> find(long surveyId, long questionTypeId,String employeeCode) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User_Result> cq = cb.createQuery(User_Result.class);
		Root<User_Result> root = cq.from(User_Result.class);
		List<Predicate> queries = new ArrayList<>();
		Predicate deleteQuery = cb.equal(root.get("isDeleted"), false);
		queries.add(deleteQuery);
		if (surveyId != 0) {
			Predicate answerTypeQuery = cb.equal(root.get("question").get("survey").get("id"), surveyId);
			queries.add(answerTypeQuery);
		}
		if (questionTypeId != 0) {
			Predicate questionTypeIdQuery = cb.equal(root.get("question").get("questionType").get("id"), questionTypeId);
			queries.add(questionTypeIdQuery);
		}
		if (employeeCode != null) {
			Predicate employeeCodeQuery = cb.equal(root.get("employeeCode"), employeeCode);
			queries.add(employeeCodeQuery);
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
	//Thong ke -> query thuan
	public List<Statistical> getStatistical(long surveyId) {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//		CriteriaQuery<User_Result> cq = cb.createQuery(User_Result.class);
//		Root<User_Result> root = cq.from(User_Result.class);
//		cq.multiselect(root.get("question").get("id"),root.get("question").get("name"),root.get("result"),cb.count(root.get("result")));
//		//predicate
//		List<Predicate> queries = new ArrayList<>();
//		Predicate deleteQuery = cb.equal(root.get("isDeleted"), false);
//		queries.add(deleteQuery);
//		if (surveyId != 0) {
//			Predicate answerTypeQuery = cb.equal(root.get("question").get("survey").get("id"), surveyId);
//			queries.add(answerTypeQuery);
//		}
//		Predicate data[] = new Predicate[queries.size()];
//		for (int i = 0; i < queries.size(); i++) {
//			data[i] = queries.get(i);
//		}
//		Predicate finalPredicate = cb.and(data);
//		cq.where(finalPredicate);
//		cq.groupBy(root.get("question").get("id"),root.get("result"));
		String sql = "Select root.id,q.id as questionId,s.name as surveyName, q.name questionName, root.result as result, count(root.result) as quantity from user_result as root, question as q, survey as s where q.survey_id = ?1 and q.id = root.question_id and q.survey_id = s.id group by root.question_id, root.result";
		Query query = em.createNativeQuery(sql,Statistical.class).setParameter(1, surveyId);
		List<Statistical> items = query.getResultList();
		return items;
	}
}