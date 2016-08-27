package org.Invoice.service;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.Invoice.springmvc.webapp.model.Account;

@Named
@SessionScoped
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1881490444979480714L;

	private Account user;

	public Account getUser() {
		return user;
	}

	public void setUser(Account user) {
		this.user = user;
	}

}
