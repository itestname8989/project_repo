package org.Invoice.springmvc.webapp.model;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import org.Invoice.springmvc.webapp.model.Project;
import javax.persistence.ManyToOne;
import org.Invoice.springmvc.webapp.model.Employee;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Enumerated;
import org.Invoice.springmvc.webapp.model.Status;

@Entity
public class NewTimeEntry implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	@Version
	@Column(name = "version")
	private int version;

	@ManyToOne
	private Project project;

	@ManyToOne
	private Employee employee;

	@Column
	private Float sun = 0f;

	@Column
	private Float mon = 0f;

	@Column
	private Float tue = 0f;

	@Column
	private Float wed = 0f;

	@Column
	private Float thu = 0f;

	@Column
	private Float fri = 0f;

	@Column
	private Float sat = 0f;

	@Column
	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Column
	@Temporal(TemporalType.DATE)
	private Date endDate;

	@Column
	private Float totalHours;

	@Enumerated
	private Status status;

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
		if (!(obj instanceof NewTimeEntry)) {
			return false;
		}
		NewTimeEntry other = (NewTimeEntry) obj;
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

	public Project getProject() {
		return this.project;
	}

	public void setProject(final Project project) {
		this.project = project;
	}

	public Employee getEmployee() {
		return this.employee;
	}

	public void setEmployee(final Employee employee) {
		this.employee = employee;
	}

	public Float getMon() {
		return mon;
	}

	public void setMon(Float mon) {
		this.mon = mon;
	}

	public Float getTue() {
		return tue;
	}

	public void setTue(Float tue) {
		this.tue = tue;
	}

	public Float getWed() {
		return wed;
	}

	public void setWed(Float wed) {
		this.wed = wed;
	}

	public Float getFri() {
		return fri;
	}

	public void setFri(Float fri) {
		this.fri = fri;
	}

	public Float getSat() {
		return sat;
	}

	public void setSat(Float sat) {
		this.sat = sat;
	}

	public Float getSun() {
		return sun;
	}

	public void setSun(Float sun) {
		this.sun = sun;
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

	public Float getTotalHours() {
		if(sun == null) sun = 0f;
		if(mon == null) mon = 0f;
		if(tue == null) tue = 0f;
		if(wed == null) wed = 0f;
		if(thu == null) thu = 0f;
		if(fri == null) fri = 0f;
		if(sat == null) sat = 0f;
		totalHours = sun +  mon +  tue +  wed +  thu +  fri +  sat;
		return totalHours;
	}

	public void setTotalHours(Float totalHours) {
		this.totalHours = getTotalHours();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Float getThu() {
		return thu;
	}

	public void setThu(Float thu) {
		this.thu = thu;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (mon != null)
			result += "mon: " + mon;
		if (tue != null)
			result += ", tue: " + tue;
		if (wed != null)
			result += ", wed: " + wed;
		if (fri != null)
			result += ", fri: " + fri;
		if (sat != null)
			result += ", sat: " + sat;
		if (sun != null)
			result += ", sun: " + sun;
		if (totalHours != null)
			result += ", totalHours: " + totalHours;
		if (thu != null)
			result += ", thu: " + thu;
		return result;
	}
}