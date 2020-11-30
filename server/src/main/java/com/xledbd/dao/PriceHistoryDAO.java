package com.xledbd.dao;

import com.xledbd.entity.PriceHistory;
import com.xledbd.entity.User;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class PriceHistoryDAO implements DAO<PriceHistory> {

	private final SessionFactory factory = SessionFactorySingleton.getInstance();

	@Override
	public List<PriceHistory> getList() {
		List<PriceHistory> list = null;
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();

			Query<PriceHistory> query =
					session.createQuery("from PriceHistory p " +
							"left join fetch p.service " +
							"order by p.id", PriceHistory.class);
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
	public int create(PriceHistory object) {
		int generatedId = -1;
		object.setDateFrom(LocalDate.now());
		object.setDateTo(null);
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
	public void save(PriceHistory object) {
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
	public PriceHistory get(int id) {
		Session session = factory.getCurrentSession();
		PriceHistory priceHistory = null;
		try {
			session.beginTransaction();
			priceHistory = session.get(PriceHistory.class, id);
			session.getTransaction().commit();
		} catch (JDBCException e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return priceHistory;
	}

	@Override
	public void delete(int id) {
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();

			Query query = session.createQuery("delete from PriceHistory where id=:priceId");
			query.setParameter("priceId", id);
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
