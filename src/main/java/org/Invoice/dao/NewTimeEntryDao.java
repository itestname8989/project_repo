package org.Invoice.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.Invoice.springmvc.webapp.model.NewTimeEntry;

/**
 * DAO for NewTimeEntry
 */
@Stateless
public class NewTimeEntryDao {
	@PersistenceContext(unitName = "Invoice-persistence-unit")
	private EntityManager em;

	public void create(NewTimeEntry entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		NewTimeEntry entity = em.find(NewTimeEntry.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public NewTimeEntry findById(Long id) {
		return em.find(NewTimeEntry.class, id);
	}

	public NewTimeEntry update(NewTimeEntry entity) {
		return em.merge(entity);
	}

	public List<NewTimeEntry> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<NewTimeEntry> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT n FROM NewTimeEntry n LEFT JOIN FETCH n.project LEFT JOIN FETCH n.employee ORDER BY n.id",
						NewTimeEntry.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
	
	public List<NewTimeEntry> listOfAllTimeEntry() {
		TypedQuery<NewTimeEntry> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT n FROM NewTimeEntry n LEFT JOIN FETCH n.project LEFT JOIN FETCH n.employee ORDER BY n.id",
						NewTimeEntry.class);
		
		return findAllQuery.getResultList();
	}
}
