package lixco.com.services;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}