package lixco.com.services;

import java.util.List;

public interface IAbstractService<T> {
	
	T findById(long id);
	
	T create(T entities);
	
	T update(T entities);
	
	boolean delete(T entities);
	
	List<T> findAll();
	
	List<T> findAllByFilter();
}