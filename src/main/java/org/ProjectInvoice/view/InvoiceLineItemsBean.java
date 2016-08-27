package org.ProjectInvoice.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.Invoice.springmvc.webapp.model.InvoiceLineItems;
import org.Invoice.springmvc.webapp.model.Invoice;
import org.Invoice.springmvc.webapp.model.InvoiceLineItemStatus;

/**
 * Backing bean for InvoiceLineItems entities.
 * <p/>
 * This class provides CRUD functionality for all InvoiceLineItems entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class InvoiceLineItemsBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving InvoiceLineItems entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private InvoiceLineItems invoiceLineItems;

	public InvoiceLineItems getInvoiceLineItems() {
		return this.invoiceLineItems;
	}

	public void setInvoiceLineItems(InvoiceLineItems invoiceLineItems) {
		this.invoiceLineItems = invoiceLineItems;
	}

	@Inject
	private Conversation conversation;

	@PersistenceContext(unitName = "Invoice-persistence-unit", type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	public String create() {

		this.conversation.begin();
		this.conversation.setTimeout(1800000L);
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.conversation.isTransient()) {
			this.conversation.begin();
			this.conversation.setTimeout(1800000L);
		}

		if (this.id == null) {
			this.invoiceLineItems = this.example;
		} else {
			this.invoiceLineItems = findById(getId());
		}
	}

	public InvoiceLineItems findById(Long id) {

		return this.entityManager.find(InvoiceLineItems.class, id);
	}

	/*
	 * Support updating and deleting InvoiceLineItems entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.invoiceLineItems);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.invoiceLineItems);
				return "view?faces-redirect=true&id="
						+ this.invoiceLineItems.getId();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public String delete() {
		this.conversation.end();

		try {
			InvoiceLineItems deletableEntity = findById(getId());

			this.entityManager.remove(deletableEntity);
			this.entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	/*
	 * Support searching InvoiceLineItems entities with pagination
	 */

	private int page;
	private long count;
	private List<InvoiceLineItems> pageItems;

	private InvoiceLineItems example = new InvoiceLineItems();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public InvoiceLineItems getExample() {
		return this.example;
	}

	public void setExample(InvoiceLineItems example) {
		this.example = example;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();

		// Populate this.count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<InvoiceLineItems> root = countCriteria
				.from(InvoiceLineItems.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<InvoiceLineItems> criteria = builder
				.createQuery(InvoiceLineItems.class);
		root = criteria.from(InvoiceLineItems.class);
		TypedQuery<InvoiceLineItems> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<InvoiceLineItems> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		InvoiceLineItemStatus status = this.example.getStatus();
		if (status != null) {
			predicatesList.add(builder.equal(root.get("status"), status));
		}
		Invoice invoice = this.example.getInvoice();
		if (invoice != null) {
			predicatesList.add(builder.equal(root.get("invoice"), invoice));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<InvoiceLineItems> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back InvoiceLineItems entities (e.g. from
	 * inside an HtmlSelectOneMenu)
	 */

	public List<InvoiceLineItems> getAll() {

		CriteriaQuery<InvoiceLineItems> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(InvoiceLineItems.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(InvoiceLineItems.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final InvoiceLineItemsBean ejbProxy = this.sessionContext
				.getBusinessObject(InvoiceLineItemsBean.class);

		return new Converter() {

			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return ejbProxy.findById(Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((InvoiceLineItems) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private InvoiceLineItems add = new InvoiceLineItems();

	public InvoiceLineItems getAdd() {
		return this.add;
	}

	public InvoiceLineItems getAdded() {
		InvoiceLineItems added = this.add;
		this.add = new InvoiceLineItems();
		return added;
	}
}
