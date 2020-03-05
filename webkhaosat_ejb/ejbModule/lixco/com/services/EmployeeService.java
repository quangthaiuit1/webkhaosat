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

import lixco.com.entities.Employee;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class EmployeeService extends AbstractService<Employee>{

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext ct;

	@Override
	protected Class<Employee> getEntityClass() {
		return Employee.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
	public List<Employee> find(String employeeCode,long surveyId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> root = cq.from(Employee.class);
		List<Predicate> queries = new ArrayList<>();
		Predicate deleteQuery = cb.equal(root.get("isDeleted"), false);
		queries.add(deleteQuery);
		if (employeeCode != null) {
			Predicate employeeCodeQuery = cb.equal(root.get("employeeCode"), employeeCode);
			queries.add(employeeCodeQuery);
		}
		if (surveyId != 0) {
			Predicate surveyIdQuery = cb.equal(root.get("surveyId"), surveyId);
			queries.add(surveyIdQuery);
		}
		Predicate data[] = new Predicate[queries.size()];
		for (int i = 0; i < queries.size(); i++) {
			data[i] = queries.get(i);
		}
		Predicate finalPredicate = cb.and(data);
		cq.where(finalPredicate);
		TypedQuery<Employee> query = em.createQuery(cq);
		query.getResultList();
		return query.getResultList();
	}
	
}
