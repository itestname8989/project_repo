package org.Invoice.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.Invoice.springmvc.webapp.model.Account;

/**
 * DAO for Account
 */
@Stateless
public class AccountDao {
	@PersistenceContext(unitName = "Invoice-persistence-unit")
	private EntityManager em;

	public void create(Account entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		Account entity = em.find(Account.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Account findById(Long id) {
		return em.find(Account.class, id);
	}

	public Account update(Account entity) {
		return em.merge(entity);
	}

	public List<Account> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Account> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT a FROM Account a LEFT JOIN FETCH a.employee ORDER BY a.id",
						Account.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
	
	public Account getUserByUsername(String username) {
		List<Account> list = listAll(0, 1000);
		for(Account a : list){
			if(a.getUserName().equals(username.trim().toLowerCase()))
					return a;
		}
		return null;
	}
}
