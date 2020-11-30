package com.xledbd.dao;

import com.xledbd.entity.User;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class UserDAO implements DAO<User> {

	private final SessionFactory factory = SessionFactorySingleton.getInstance();

	@Override
	public List<User> getList() {
		List<User> list = null;
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();

			Query<User> query =
					session.createQuery("from User u order by u.id", User.class);
			list = query.getResultList();

			session.getTransaction().commit();
		} catch (JDBCException e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public int create(User object) {
		int generatedId = -1;
		object.setAccessLevel(0);
		object.setRegistrationDate(LocalDateTime.now());
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();
			generatedId = (Integer)session.save(object);
			session.getTransaction().commit();
		} catch (JDBCException e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return generatedId;
	}

	@Override
	public void save(User object) {
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();
			session.update(object);
			session.getTransaction().commit();
		} catch (JDBCException e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@Override
	public User get(int id) {
		Session session = factory.getCurrentSession();
		User user = null;
		try {
			session.beginTransaction();
			user = session.get(User.class, id);
			session.getTransaction().commit();
		} catch (JDBCException e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return user;
	}

	@Override
	public void delete(int id) {
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();

			Query query = session.createQuery("delete from User where id=:userId");
			query.setParameter("userId", id);
			query.executeUpdate();

			session.getTransaction().commit();
		} catch (JDBCException e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}
