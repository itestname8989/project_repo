package org.Invoice.service;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.Invoice.dao.AccountDao;
import org.Invoice.springmvc.webapp.model.Account;

@Named
@SessionScoped
public class AuthService implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2592966932274039905L;

	@Inject
	AccountDao accountDao;
	
	private Account account;
	
	
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Account isAuthenticated(Account account){
		if(account.getUserName() == null || account.getPassword() == null || account.getUserName().trim().equals("") || account.getPassword().trim().equals(""))
			return null;
		Account a = accountDao.getUserByUsername(account.getUserName());
		if(a == null)
			return null;
		if(a.getUserName().trim().toLowerCase().equals(account.getUserName().trim().toLowerCase()) && a.getPassword().trim().equals(account.getPassword().trim()))
		{
			setAccount(a);
			return a;
		}
		else
			return null;
	}
	
	
	public void validateUserLogin() {
		// TODO Auto-generated method stub
		String login = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		if(login.equals("/account/login.xhtml"))
			return;	
		
		Account account = this.getAccount();	
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		String contextUrl = context.getRequestContextPath();
		
		if(login.equals(contextUrl) || login.equals("/index.xhtml"))
			return;

		if (account == null) {

			try {
				context.redirect(contextUrl);// + "/faces/account/login.xhtml");				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	
	/*this is to process logout
	 */
	public void processLogout(){
		this.setAccount(null);
		this.validateUserLogin();
	}
	
	/*@Override
	public void processAction(ActionEvent arg0) throws AbortProcessingException {
		// 
		
		
	}*/
}
