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

import org.Invoice.springmvc.webapp.model.NewTimeEntry;
import org.Invoice.springmvc.webapp.model.Employee;
import org.Invoice.springmvc.webapp.model.Project;
import org.Invoice.springmvc.webapp.model.Status;

/**
 * Backing bean for NewTimeEntry entities.
 * <p/>
 * This class provides CRUD functionality for all NewTimeEntry entities. It
 * focuses purely on Java EE 6 standards (e.g. <tt>&#64;ConversationScoped</tt>
 * for state management, <tt>PersistenceContext</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@Stateful
@ConversationScoped
public class NewTimeEntryBean implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving NewTimeEntry entities
	 */

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private NewTimeEntry newTimeEntry;

	public NewTimeEntry getNewTimeEntry() {
		return this.newTimeEntry;
	}

	public void setNewTimeEntry(NewTimeEntry newTimeEntry) {
		this.newTimeEntry = newTimeEntry;
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
			this.newTimeEntry = this.example;
		} else {
			this.newTimeEntry = findById(getId());
		}
	}

	public NewTimeEntry findById(Long id) {

		return this.entityManager.find(NewTimeEntry.class, id);
	}

	/*
	 * Support updating and deleting NewTimeEntry entities
	 */

	public String update() {
		this.conversation.end();

		try {
			if (this.id == null) {
				this.entityManager.persist(this.newTimeEntry);
				return "search?faces-redirect=true";
			} else {
				this.entityManager.merge(this.newTimeEntry);
				return "view?faces-redirect=true&id="
						+ this.newTimeEntry.getId();
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
			NewTimeEntry deletableEntity = findById(getId());

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
	 * Support searching NewTimeEntry entities with pagination
	 */

	private int page;
	private long count;
	private List<NewTimeEntry> pageItems;

	private NewTimeEntry example = new NewTimeEntry();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public NewTimeEntry getExample() {
		return this.example;
	}

	public void setExample(NewTimeEntry example) {
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
		Root<NewTimeEntry> root = countCriteria.from(NewTimeEntry.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		this.count = this.entityManager.createQuery(countCriteria)
				.getSingleResult();

		// Populate this.pageItems

		CriteriaQuery<NewTimeEntry> criteria = builder
				.createQuery(NewTimeEntry.class);
		root = criteria.from(NewTimeEntry.class);
		TypedQuery<NewTimeEntry> query = this.entityManager
				.createQuery(criteria.select(root).where(
						getSearchPredicates(root)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<NewTimeEntry> root) {

		CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		Project project = this.example.getProject();
		if (project != null) {
			predicatesList.add(builder.equal(root.get("project"), project));
		}
		Employee employee = this.example.getEmployee();
		if (employee != null) {
			predicatesList.add(builder.equal(root.get("employee"), employee));
		}
		Status status = this.example.getStatus();
		if (status != null) {
			predicatesList.add(builder.equal(root.get("status"), status));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<NewTimeEntry> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back NewTimeEntry entities (e.g. from inside
	 * an HtmlSelectOneMenu)
	 */

	public List<NewTimeEntry> getAll() {

		CriteriaQuery<NewTimeEntry> criteria = this.entityManager
				.getCriteriaBuilder().createQuery(NewTimeEntry.class);
		return this.entityManager.createQuery(
				criteria.select(criteria.from(NewTimeEntry.class)))
				.getResultList();
	}

	@Resource
	private SessionContext sessionContext;

	public Converter getConverter() {

		final NewTimeEntryBean ejbProxy = this.sessionContext
				.getBusinessObject(NewTimeEntryBean.class);

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

				return String.valueOf(((NewTimeEntry) value).getId());
			}
		};
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private NewTimeEntry add = new NewTimeEntry();

	public NewTimeEntry getAdd() {
		return this.add;
	}

	public NewTimeEntry getAdded() {
		NewTimeEntry added = this.add;
		this.add = new NewTimeEntry();
		return added;
	}
}
