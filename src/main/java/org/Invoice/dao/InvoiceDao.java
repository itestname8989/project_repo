package org.Invoice.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

import org.Invoice.springmvc.webapp.model.Client;
import org.Invoice.springmvc.webapp.model.Invoice;

/**
 * DAO for Invoice
 */
@Stateful
public class InvoiceDao  {
	@PersistenceContext(unitName = "Invoice-persistence-unit", type=PersistenceContextType.EXTENDED)
	private EntityManager em;

	public void create(Invoice entity) {
		
		em.persist(entity);
	}
	
	

	public void deleteById(Long id) {
		Invoice entity = em.find(Invoice.class, id);
		if (entity != null) {
			em.remove(entity);
		}
	}

	public Invoice findById(Long id) {
		return em.find(Invoice.class, id);
	}


	public Invoice update(Invoice entity) {
		return em.merge(entity);
	}

	public List<Invoice> listAll(Integer startPosition, Integer maxResult) {
		TypedQuery<Invoice> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT i FROM Invoice i LEFT JOIN FETCH i.client LEFT JOIN FETCH i.invoiceLineItems ORDER BY i.id",
						Invoice.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		return findAllQuery.getResultList();
	}
	
	public List<Invoice> listOfAllinvoices() {
		TypedQuery<Invoice> findAllQuery = em
				.createQuery(
						"SELECT DISTINCT i FROM Invoice i LEFT JOIN FETCH i.client LEFT JOIN FETCH i.invoiceLineItems ORDER BY i.id",
						Invoice.class);
		
		return findAllQuery.getResultList();
	}
	
	public Invoice lastInvoiceByClient(Client c){
		Invoice invoice = null;
		List<Invoice> in =  listOfAllinvoices();
		Date date = null;
		if(in == null)
			return null;
		else{
			
			for(Invoice i : in){
				if(i.getClient().getId() == c.getId())
				{
					if(date == null ){
						date = i.getDate();
						invoice = i;
					}
					else{
						if(i.getDate().after(date))
						{
							date = i.getDate();
							invoice = i;
						}
						
					}
					
				}
			}
			return invoice;
			
		}
		
		/*Invoice invoice = null;
		try {
			TypedQuery<Invoice> findAllQuery = em
					.createQuery(
							"SELECT DISTINCT i FROM Invoice i WHERE i.client.id =:clientId ORDER BY i.date desc",
							Invoice.class).setMaxResults(1);
			findAllQuery.setParameter("clientId", c.getId());
			
			List<Invoice> i =  findAllQuery.getResultList();// .getSingleResult();
			if(i != null)
				return i.get(0);
		} catch (Exception e) {
			System.out.println("Error on lastInvoiceByClient in invoiceDao");
			
			sysout
		}
		
		
		return null;*/
		
		//return list;
	}
}
