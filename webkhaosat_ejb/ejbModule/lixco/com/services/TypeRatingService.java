package lixco.com.services;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lixco.com.entities.TypeRating;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class TypeRatingService extends AbstractService<TypeRating>{

	@PersistenceContext
	private EntityManager em;

	@Resource
	private SessionContext ct;

	@Override
	protected Class<TypeRating> getEntityClass() {
		return TypeRating.class;
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
