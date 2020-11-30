package com.xledbd.dao;

import com.xledbd.entity.Contract;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class ContractDAO implements DAO<Contract> {

	private final SessionFactory factory = SessionFactorySingleton.getInstance();

	@Override
	public List<Contract> getList() {
		List<Contract> list = null;
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();

			Query<Contract> query =
					session.createQuery("from Contract c " +
							"left join fetch c.service " +
							"left join fetch c.user " +
							"order by c.id", Contract.class);
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
	public int create(Contract object) {
		int generatedId = -1;
		object.setDateFrom(LocalDate.now());
		object.setDateTo(null);
		object.setBalance(0.01);
		object.setActive(true);
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
	public void save(Contract object) {
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
	public Contract get(int id) {
		Session session = factory.getCurrentSession();
		Contract contract = null;
		try {
			session.beginTransaction();
			contract = session.get(Contract.class, id);
			session.getTransaction().commit();
		} catch (JDBCException e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return contract;
	}

	@Override
	public void delete(int id) {
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();

			Query query = session.createQuery("delete from Contract where id=:conId");
			query.setParameter("conId", id);
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
