package org.Invoice.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.Invoice.springmvc.webapp.model.Client;

/**
 * DAO for Client
 */
@Stateless
public class ClientDao {
	@PersistenceContext(unitName = "Invoice-persistence-unit")
	private EntityManager em;

	public void create(Client entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		Client entity = em.find(Client.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Client findById(Long id) {
		return em.find(Client.class, id);
	}

	public Client update(Client entity) {
		return em.merge(entity);
	}

	public List<Client> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Client> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.projects LEFT JOIN FETCH c.company ORDER BY c.id",
						Client.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
	
	public  List<Client> getAll() {
		TypedQuery<Client> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.projects LEFT JOIN FETCH c.company ORDER BY c.id",
						Client.class);
		return findAllQuery.getResultList();
	}
}
