package org.Invoice.dao;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

import org.Invoice.springmvc.webapp.model.Employee;
import org.Invoice.springmvc.webapp.model.InvoiceLineItemStatus;
import org.Invoice.springmvc.webapp.model.InvoiceLineItems;
import org.Invoice.springmvc.webapp.model.Project;
import org.omg.IOP.TransactionService;

/**
 * DAO for InvoiceLineItems
 */
@Stateful
public class InvoiceLineItemsDao  {
	@PersistenceContext(unitName = "Invoice-persistence-unit", type=PersistenceContextType.EXTENDED)
	private EntityManager em;

	public void create(InvoiceLineItems entity) {
		em.persist(entity);
	}

	public void deleteById(Long id) {
		InvoiceLineItems entity = em.find(InvoiceLineItems.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public InvoiceLineItems findById(Long id) {
		return em.find(InvoiceLineItems.class, id);
	}

	public InvoiceLineItems update(InvoiceLineItems entity) {
		return em.merge(entity);
	}

	public List<InvoiceLineItems> listAll(Integer startPosition,
			Integer maxResult) {
		TypedQuery<InvoiceLineItems> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT i FROM InvoiceLineItems i LEFT JOIN FETCH i.employee LEFT JOIN FETCH i.project LEFT JOIN FETCH i.invoice ORDER BY i.id",
						InvoiceLineItems.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
	
	public List<InvoiceLineItems> listNotInvoiceGeratedAllByProjectId(Project p, Employee e) {
		String query = "";
		try {
			TypedQuery<InvoiceLineItems> findAllQuery = em
					.createQuery(
							"SELECT DISTINCT i FROM InvoiceLineItems i LEFT JOIN FETCH i.employee WHERE i.employee.id =:eId  and  i.project.id =:pId and i.status =:status ORDER BY i.id",
							InvoiceLineItems.class);
			findAllQuery.setParameter("eId", e.getId());
			findAllQuery.setParameter("pId", p.getId());
			findAllQuery.setParameter("status", InvoiceLineItemStatus.INVOICE_NOT_GENERATED);
			
			query = findAllQuery.toString();
			return findAllQuery.getResultList();
			
			
		} catch (Exception e2) {
			System.out.println("Query: = " + query);
			System.out.println(p.getId() + " " + p.toString());
			System.out.println(e.getId() + " " + e.toString());
			
			 
		}
		
		return null;
		
	}
}
