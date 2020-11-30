package com.xledbd.dao;

import com.xledbd.entity.Invoice;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class InvoiceDAO implements DAO<Invoice> {

	private final SessionFactory factory = SessionFactorySingleton.getInstance();

	@Override
	public List<Invoice> getList() {
		List<Invoice> list = null;
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();

			Query<Invoice> query =
					session.createQuery("from Invoice i " +
							"left join fetch i.contract " +
							"left join fetch i.transaction " +
							"order by i.id", Invoice.class);
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

	// TODO: Implement create
	@Override
	public int create(Invoice object) {
		return 0;
	}

	@Override
	public void save(Invoice object) {
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
	public Invoice get(int id) {
		Session session = factory.getCurrentSession();
		Invoice invoice = null;
		try {
			session.beginTransaction();
			invoice = session.get(Invoice.class, id);
			session.getTransaction().commit();
		} catch (JDBCException e) {
			session.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return invoice;
	}

	@Override
	public void delete(int id) {
		Session session = factory.getCurrentSession();
		try {
			session.beginTransaction();

			Query query = session.createQuery("delete from Invoice where id=:invoiceId");
			query.setParameter("invoiceId", id);
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
