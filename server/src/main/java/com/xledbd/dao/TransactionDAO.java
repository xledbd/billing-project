package com.xledbd.dao;

import com.xledbd.entity.Transaction;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionDAO implements DAO<Transaction> {

	private final SessionFactory factory = SessionFactorySingleton.getInstance();

	@Override
	public List<Transaction> getList() {
		List<Transaction> list = null;
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();
			Query<Transaction> query =
					session.createQuery("from Transaction t order by t.id", Transaction.class);
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
	public int create(Transaction object) {
		int generatedId = -1;
		object.setDate(LocalDateTime.now());
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
	public void save(Transaction object) {
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
	public Transaction get(int id) {
		Transaction transaction = null;
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();
			transaction = session.get(Transaction.class, id);
			session.getTransaction().commit();
		} catch (JDBCException e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return transaction;
	}

	@Override
	public void delete(int id) {
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();

			Query query = session.createQuery("delete from Transaction where id=:tranId");
			query.setParameter("tranId", id);
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
