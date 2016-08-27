package org.Invoice.springmvc.webapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import javax.persistence.Enumerated;
import org.Invoice.springmvc.webapp.model.ProjectStatus;

@Entity
@XmlRootElement
public class Project implements Serializable {

	@Enumerated
	private ProjectStatus status;

	public Project() {

	}

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@Column(name = "project_number", nullable = false)
	private Integer number;

	@Column(nullable = false)
	private String name;

	@Column
	private Double budget;

	@Column
	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Column
	@Temporal(TemporalType.DATE)
	private Date endDate;

	@ManyToOne
	private Client client;

	@ManyToMany(targetEntity = Employee.class, cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinTable(name = "project_employee", joinColumns = @JoinColumn(name = "project_id"), inverseJoinColumns = @JoinColumn(name = "employee_id"))
	private Set<Employee> employee = new HashSet<Employee>();

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
		if (!(obj instanceof Project)) {
			return false;
		}
		Project other = (Project) obj;
		if (number != null) {
			if (!number.equals(other.number)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return number != null ? number.hashCode() : 0;
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

	public Double getBudget() {
		return budget;
	}

	public void setBudget(Double budget) {
		this.budget = budget;
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

	public Client getClient() {
		return this.client;
	}

	public void setClient(final Client client) {
		this.client = client;
	}

	public Set<Employee> getEmployee() {
		return this.employee;
	}

	public void setEmployee(final Set<Employee> employee) {
		this.employee = employee;
	}

	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (number != null)
			result += "number: " + number;
		if (name != null && !name.trim().isEmpty())
			result += ", name: " + name;
		if (budget != null)
			result += ", budget: " + budget;
		return result;
	}
}