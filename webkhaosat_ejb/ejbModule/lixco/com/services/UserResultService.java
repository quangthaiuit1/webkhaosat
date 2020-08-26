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
import lixco.com.entities.temp.UserResultByDepartment;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class UserResultService extends AbstractService<User_Result> {

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

	public List<User_Result> find(long surveyId, long questionTypeId, String employeeCode, String departmentName) {

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
			Predicate questionTypeIdQuery = cb.equal(root.get("question").get("questionType").get("id"),
					questionTypeId);
			queries.add(questionTypeIdQuery);
		}
		if (employeeCode != null) {
			Predicate employeeCodeQuery = cb.equal(root.get("employeeCode"), employeeCode);
			queries.add(employeeCodeQuery);
		}
		if (departmentName != null) {
			Predicate departmentNameQuery = cb.equal(root.get("departmentName"), departmentName);
			queries.add(departmentNameQuery);
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

	// Thong ke -> query thuan
	@SuppressWarnings("unchecked")
	public List<Statistical> getStatistical(long surveyId) {
		String sql = "Select root.id,q.id as questionId,s.name as surveyName, q.name questionName, root.result as result, count(root.result) as quantity from user_result as root, question as q, survey as s where q.survey_id = ?1 and q.id = root.question_id and q.survey_id = s.id group by root.question_id, root.result";
		Query query = em.createNativeQuery(sql, Statistical.class).setParameter(1, surveyId);
		List<Statistical> items = query.getResultList();
		return items;
	}
	
	public List<User_Result> findByResult(long surveyId, String result) {

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
		if (result != null) {
			Predicate departmentNameQuery = cb.equal(root.get("result"), result);
			queries.add(departmentNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.select(root).where(finalPredicate).orderBy(cb.asc(root.get("employeeName")));
		TypedQuery<User_Result> query = em.createQuery(cq);
		List<User_Result> resultEnd = query.getResultList();
		if(resultEnd.isEmpty()) {
			return new ArrayList<>();
		}else {
			return resultEnd;
		}
	}
	public int findByCountResult(long surveyId) {

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
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.select(root).where(finalPredicate).orderBy(cb.asc(root.get("employeeName")));
		TypedQuery<User_Result> query = em.createQuery(cq);
		List<User_Result> resultEnd = query.getResultList();
		if(resultEnd.isEmpty()) {
			return 0;
		}else {
			return resultEnd.size();
		}
	}
	
//	@SuppressWarnings("unchecked")
//	public List<UserResultByDepartment> findByDepartmentCode(long surveyId, String departmentCode) {
//		String sql = "SELECT u.employee_code,u.employee_name  FROM user_result as u where u.survey_id = ?1 and u.employee_code = ?2";
//		Query query = em.createNativeQuery(sql, UserResultByDepartment.class).setParameter(1, surveyId);
//		query.setParameter(2, departmentCode);
//		List<UserResultByDepartment> items = query.getResultList();
//		return items;
//	}

	public List<String> findByDepartmentCode(long surveyId, String departmentCode) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<User_Result> root = cq.from(User_Result.class);
		List<Predicate> queries = new ArrayList<>();
		Predicate deleteQuery = cb.equal(root.get("isDeleted"), false);
		queries.add(deleteQuery);
		if (surveyId != 0) {
			Predicate answerTypeQuery = cb.equal(root.get("question").get("survey").get("id"), surveyId);
			queries.add(answerTypeQuery);
		}
		if (departmentCode != null) {
			Predicate departmentNameQuery = cb.equal(root.get("departmentCode"), departmentCode);
			queries.add(departmentNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		cq.select(root.get("employeeCode")).distinct(true);
//		cq.multiselect(root.get("employeeName"),root.get("employeeCode")).distinct(true);
		TypedQuery<String> query = em.createQuery(cq);
		List<String> result = query.getResultList();
		if (!result.isEmpty()) {
			return result;
		} else {
			return new ArrayList<>();
		}
	}
	
	public List<String> findByDepartmentName(long surveyId, String departmentName) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<User_Result> root = cq.from(User_Result.class);
		List<Predicate> queries = new ArrayList<>();
		Predicate deleteQuery = cb.equal(root.get("isDeleted"), false);
		queries.add(deleteQuery);
		if (surveyId != 0) {
			Predicate answerTypeQuery = cb.equal(root.get("question").get("survey").get("id"), surveyId);
			queries.add(answerTypeQuery);
		}
		if (departmentName != null) {
			Predicate departmentNameQuery = cb.equal(root.get("departmentName"), departmentName);
			queries.add(departmentNameQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		cq.select(root.get("employeeCode")).distinct(true);
//		cq.multiselect(root.get("employeeName"),root.get("employeeCode")).distinct(true);
		TypedQuery<String> query = em.createQuery(cq);
		List<String> result = query.getResultList();
		if (!result.isEmpty()) {
			return result;
		} else {
			return new ArrayList<>();
		}
	}

}