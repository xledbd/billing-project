package com.xledbd.dao;

import java.util.List;

public interface DAO<T> {
	List<T> getList();
	int create(T object);
	void save(T object);
	T get(int id);
	void delete(int id);
}
