package org.Invoice.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.Invoice.springmvc.webapp.model.Employee;

/**
 * DAO for Employee
 */
@Stateless
public class EmployeeDao {
	@PersistenceContext(unitName = "Invoice-persistence-unit")
	private EntityManager em;

	public void create(Employee entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		Employee entity = em.find(Employee.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Employee findById(Long id) {
		return em.find(Employee.class, id);
	}

	public Employee update(Employee entity) {
		return em.merge(entity);
	}

	public List<Employee> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Employee> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT e FROM Employee e LEFT JOIN FETCH e.projects ORDER BY e.id",
						Employee.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
}
