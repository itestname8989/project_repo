package org.Invoice.springmvc.webapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Invoice implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@ManyToOne
	private Client client;

	@Column(name = "invoice_date ")
	@Temporal(TemporalType.DATE)
	private Date date;

	@OneToMany
	private Set<InvoiceLineItems> invoiceLineItems = new HashSet<InvoiceLineItems>();

	@Column(name = "invoice_number")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String number;

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
		if (!(obj instanceof Invoice)) {
			return false;
		}
		Invoice other = (Invoice) obj;
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

	public Client getClient() {
		return this.client;
	}

	public void setClient(final Client client) {
		this.client = client;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Set<InvoiceLineItems> getInvoiceLineItems() {
		return this.invoiceLineItems;
	}

	public void setInvoiceLineItems(final Set<InvoiceLineItems> invoiceLineItems) {
		this.invoiceLineItems = invoiceLineItems;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (number != null && !number.trim().isEmpty())
			result += "number: " + number;
		return result;
	}
}