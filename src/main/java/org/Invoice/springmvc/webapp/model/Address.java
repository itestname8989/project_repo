package org.Invoice.springmvc.webapp.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Column;

/**
 * <p>
 * A reusable representation of an address.
 * </p>
 * 
 * <p>
 * Addresses are used in many places in an application, so to observe the DRY
 * principle, we model Address as an embeddable entity. An embeddable entity
 * appears as a child in the object model, but no relationship is established in
 * the RDBMS..
 * </p>
 * 
  */
@SuppressWarnings("serial")
@Embeddable
public class Address implements Serializable {

	private String city;
	/* Declaration of boilerplate getters and setters */

	@Column
	private String addressLine2;

	@Column
	private String addressLine1;

	@Column
	private String state;

	@Column
	private String zip;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	/*
	 * toString(), equals() and hashCode() for Address, using the natural
	 * identity of the object
	 */

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Address address = (Address) o;

		if (city != null ? !city.equals(address.city) : address.city != null)
			return false;
		if (state != null
				? !state.equals(address.state)
				: address.state != null)
			return false;
		if (addressLine1 != null
				? !addressLine1.equals(address.addressLine1)
				: address.addressLine1 != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = addressLine1 != null ? addressLine1.hashCode() : 0;
		result = 31 * result + (city != null ? city.hashCode() : 0);
		result = 31 * result + (state != null ? state.hashCode() : 0);
		return result;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (city != null && !city.trim().isEmpty())
			result += "city: " + city;
		if (addressLine2 != null && !addressLine2.trim().isEmpty())
			result += ", addressLine2: " + addressLine2;
		if (addressLine1 != null && !addressLine1.trim().isEmpty())
			result += ", addressLine1: " + addressLine1;
		if (state != null && !state.trim().isEmpty())
			result += ", state: " + state;
		if (zip != null && !zip.trim().isEmpty())
			result += ", zip: " + zip;
		return result;
	}
}
