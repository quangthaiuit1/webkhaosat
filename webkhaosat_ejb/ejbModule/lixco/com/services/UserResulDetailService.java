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

import lixco.com.entities.UserResultDetail;
import lixco.com.entities.User_Result;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class UserResulDetailService extends AbstractService<UserResultDetail> {

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext ct;

	@Override
	protected Class<UserResultDetail> getEntityClass() {
		return UserResultDetail.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}

	public List<UserResultDetail> find(long userResultId, long surveyId, long questionId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserResultDetail> cq = cb.createQuery(UserResultDetail.class);
		Root<UserResultDetail> root = cq.from(UserResultDetail.class);
		List<Predicate> queries = new ArrayList<>();
		Predicate deleteQuery = cb.equal(root.get("isDeleted"), false);
		queries.add(deleteQuery);
		if (userResultId != 0) {
			Predicate answerTypeQuery = cb.equal(root.get("user_result").get("id"), userResultId);
			queries.add(answerTypeQuery);
		}
		if (surveyId != 0) {
			Predicate answerTypeQuery = cb.equal(root.get("survey").get("id"), surveyId);
			queries.add(answerTypeQuery);
		}
		if (questionId != 0) {
			Predicate answerTypeQuery = cb.equal(root.get("question").get("id"), questionId);
			queries.add(answerTypeQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<UserResultDetail> query = em.createQuery(cq);
		List<UserResultDetail> result = query.getResultList();
		if (result.isEmpty()) {
			return new ArrayList<>();
		} else {
			return result;
		}
	}

	public List<UserResultDetail> findDapAnLayYKien(long surveyId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserResultDetail> cq = cb.createQuery(UserResultDetail.class);
		Root<UserResultDetail> root = cq.from(UserResultDetail.class);
		List<Predicate> queries = new ArrayList<>();
		Predicate deleteQuery = cb.equal(root.get("isDeleted"), false);
		queries.add(deleteQuery);
		if (surveyId != 0) {
			Predicate answerTypeQuery = cb.equal(root.get("survey").get("id"), surveyId);
			queries.add(answerTypeQuery);
		}
		//lay danh sach dap an cua loai cau hoi co y kien
		Predicate answerTypeRating = cb.equal(root.get("question").get("questionType").get("id"), 1);
		queries.add(answerTypeRating);
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<UserResultDetail> query = em.createQuery(cq);
		List<UserResultDetail> result = query.getResultList();
		if (result.isEmpty()) {
			return new ArrayList<>();
		} else {
			return result;
		}
	}
	
	//Thong ke
	public List<UserResultDetail> thongKe(long surveyId) {

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserResultDetail> cq = cb.createQuery(UserResultDetail.class);
		Root<UserResultDetail> root = cq.from(UserResultDetail.class);
		List<Predicate> queries = new ArrayList<>();
		Predicate deleteQuery = cb.equal(root.get("isDeleted"), false);
		queries.add(deleteQuery);
		if (surveyId != 0) {
			Predicate answerTypeQuery = cb.equal(root.get("survey").get("id"), surveyId);
			queries.add(answerTypeQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
//		cq.select(root).where(finalPredicate).groupBy());
		
		TypedQuery<UserResultDetail> query = em.createQuery(cq);
		List<UserResultDetail> result = query.getResultList();
		if (result.isEmpty()) {
			return new ArrayList<>();
		} else {
			return result;
		}
	}


}
