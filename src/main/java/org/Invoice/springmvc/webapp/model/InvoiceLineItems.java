package org.Invoice.springmvc.webapp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import org.Invoice.springmvc.webapp.model.Invoice;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;

@Entity
@XmlRootElement
public class InvoiceLineItems implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@Column
	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Column
	@Temporal(TemporalType.DATE)
	private Date endDate;

	@Column
	private Float hours;

	@Column
	private Double amount;

	@Column
	private Float rate;

	@OneToOne(cascade = CascadeType.ALL)
	private Employee employee;

	@OneToOne(cascade = CascadeType.ALL)
	private Project project;

	@Enumerated
	private InvoiceLineItemStatus status;

	@ManyToOne(fetch = FetchType.LAZY)
	private Invoice invoice;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof InvoiceLineItems)) {
			return false;
		}
		InvoiceLineItems other = (InvoiceLineItems) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Float getHours() {
		return hours;
	}

	public void setHours(Float hours) {
		this.hours = hours;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Float getRate() {
		return rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void newEmployee() {
		this.employee = new Employee();
	}

	public void newProject() {
		this.project = new Project();
	}

	public InvoiceLineItemStatus getStatus() {
		return status;
	}

	public void setStatus(InvoiceLineItemStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (hours != null)
			result += "hours: " + hours;
		if (amount != null)
			result += ", amount: " + amount;
		if (rate != null)
			result += ", rate: " + rate;
		return result;
	}

	public Invoice getInvoice() {
		return this.invoice;
	}

	public void setInvoice(final Invoice invoice) {
		this.invoice = invoice;
	}
	
	public InvoiceLineItems(InvoiceLineItems item){
		this.amount = item.amount;
		this.employee = item.employee;
		this.endDate = item.endDate;
		this.hours = item.hours;
		this.id = item.id;
		this.invoice = item.invoice;
		this.project = item.project;
		this.rate = item.rate;
		this.startDate = item.startDate;
		this.version = item.version;
	}
	
	public InvoiceLineItems(){
		
	}

}