package lixco.com.services;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lixco.com.entities.QuestionType;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class QuestionTypeService extends AbstractService<QuestionType>{
	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext ct;

	@Override
	protected Class<QuestionType> getEntityClass() {
		return QuestionType.class;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	protected SessionContext getUt() {
		return ct;
	}
}
