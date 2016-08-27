package org.Invoice.springmvc.webapp.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Client implements Serializable {

	@Enumerated
	private InvoiceGrouping invoiceGrouping;

	// public Client(Integer number, String name, String contact, String email,
	// Address address, String billingTerms, String invoiceFreq,
	// String invoiceGrouping, Company company) {
	// this();
	// this.number = number;
	// this.name = name;
	// this.contact = contact;
	// this.email = email;
	// this.address = address;
	// this.billingTerms.valueOf(billingTerms);
	// this.invoiceFreq.valueOf(invoiceFreq);
	// this.invoiceGrouping.valueOf(invoiceGrouping);
	// this.company = company;
	// company.addClient(this);
	// }

	public Client() {

	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@Column(name = "clinet_number")
	private Integer number;

	@Column
	private String name;

	@Column
	private String contact;

	@Column
	private String email;

	@Embedded
	private Address address = new Address();

	@OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Project.class)
	// @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval
	// = true)
	private Set<Project> projects = new HashSet<Project>();

	@Enumerated
	private BillingTerms billingTerms;

	@Enumerated
	private InvoiceFreq invoiceFreq;

	@ManyToOne
	private Company company;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

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
		if (!(obj instanceof Client)) {
			return false;
		}
		Client other = (Client) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (address != null ? address.hashCode() : 0);
		return result;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Project> getProjects() {
		return this.projects;
	}

	public void setProjects(final Set<Project> projects) {
		this.projects = projects;
	}

	public BillingTerms getBillingTerms() {
		return billingTerms;
	}

	public void setBillingTerms(BillingTerms billingTerms) {
		this.billingTerms = billingTerms;
	}

	public InvoiceFreq getInvoiceFreq() {
		return invoiceFreq;
	}

	public void setInvoiceFreq(InvoiceFreq invoiceFreq) {
		this.invoiceFreq = invoiceFreq;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(final Company company) {
		this.company = company;
	}

	public InvoiceGrouping getInvoiceGrouping() {
		return invoiceGrouping;
	}

	public void setInvoiceGrouping(InvoiceGrouping invoiceGrouping) {
		this.invoiceGrouping = invoiceGrouping;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (number != null)
			result += "number: " + number;
		if (name != null && !name.trim().isEmpty())
			result += ", name: " + name;
		if (contact != null && !contact.trim().isEmpty())
			result += ", contact: " + contact;
		if (email != null && !email.trim().isEmpty())
			result += ", email: " + email;
		return result;
	}

	// public void addProject(Project project) {
	// ProjectDao.create(project);
	// }
}