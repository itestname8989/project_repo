package org.Invoice.dao;


import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.Invoice.springmvc.webapp.model.Project;

/**
 * DAO for Project
 */
@Stateless
public class ProjectDao {
	@PersistenceContext(unitName = "Invoice-persistence-unit")
	private EntityManager em;

	public void create(Project entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		Project entity = em.find(Project.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Project findById(Long id) {
		return em.find(Project.class, id);
	}

	public Project update(Project entity) {
		return em.merge(entity);
	}

	public List<Project> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Project> findAllQuery = em.createQuery(
				"SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.client LEFT JOIN FETCH p.employee ORDER BY p.id",
				Project.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
}
