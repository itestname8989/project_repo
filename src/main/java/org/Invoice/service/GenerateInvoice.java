package org.Invoice.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.Invoice.dao.ClientDao;
import org.Invoice.dao.EmployeeDao;
import org.Invoice.dao.InvoiceDao;
import org.Invoice.dao.InvoiceLineItemsDao;
import org.Invoice.dao.NewTimeEntryDao;
import org.Invoice.dao.ProjectDao;
import org.Invoice.springmvc.webapp.model.Client;
import org.Invoice.springmvc.webapp.model.Employee;
import org.Invoice.springmvc.webapp.model.Invoice;
import org.Invoice.springmvc.webapp.model.InvoiceFreq;
import org.Invoice.springmvc.webapp.model.InvoiceLineItemStatus;
import org.Invoice.springmvc.webapp.model.InvoiceLineItems;
import org.Invoice.springmvc.webapp.model.NewTimeEntry;
import org.Invoice.springmvc.webapp.model.Project;
import org.Invoice.springmvc.webapp.model.Status;

@Named
@Stateful
@ConversationScoped
public class GenerateInvoice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	EmployeeDao employeeDao;

	@Inject
	ClientDao clientDao;

	@Inject
	NewTimeEntryDao timeEntryDao;
	@Inject
	NewTimeEntryDao newTimeEntryDao;

	@Inject
	InvoiceDao invoiceDao;

	@Inject
	InvoiceLineItemsDao invoiceLineItemsDao;

	Date date = new Date();
	Date todaysDate = new Date();

	
	public void processLineItemEntry(){
		for (InvoiceFreq fre : InvoiceFreq.values()) {
			addInvoiceLineItems(fre);
		}
	}
	
	public void processInvoiceGeneration(){
		int counter = 0;
		for (Client c : clientDao.getAll()) {
			//System.out.println("client list " + counter);
			try {
				generateInvoiceForAClient(c);
			} catch (Exception e) {
				System.out.println("invoice generation error  on " + counter);
			}
			
			counter++;
		}
	}
	

	public List<NewTimeEntry> totalweeklyByEmployee(List<NewTimeEntry> timeEntrys, List<Client> clients) {
		for (Client c : clients) {
			for (NewTimeEntry t : timeEntrys) {
				List<NewTimeEntry> temptime;
				if (t.getStatus().APPROVED != null) {

				}

			}
		}

		return null;
	}

	public void generateInvoiceMain() {

		try {
			processLineItemEntry();
		} catch (Exception e) {
			
		}
		
		try {
			processInvoiceGeneration();
		} catch (Exception e) {
			
		}
		
	}

	private List<Invoice> generateInvoiceForAClient(Client c) {
		List<Invoice> toCreate = new ArrayList<>();
		List<InvoiceLineItems> toUpdate = new ArrayList<>();
		Invoice lastInvoice = invoiceDao.lastInvoiceByClient(c);
		Date invoiceDate = null;
		if (lastInvoice != null)
			invoiceDate = getCurrentBillingDate(c.getInvoiceFreq(), lastInvoice.getDate());

		// generateInvoiceLineItem(clients,timeEntrys);

		

		Invoice invoice = null;
		List<Invoice> invoices = new ArrayList<>();
		Set<InvoiceLineItems> invoiceLis;
		for (Project p : c.getProjects()) {
			processInvoicePerProject(p, c, invoiceDate,lastInvoice);
		}

		
	/*//	update lineItem
		for(Invoice i : toCreate){
			invoiceDao.create(i);
		}
		
		for(InvoiceLineItems ils : toUpdate){
			invoiceLineItemsDao.update(ils);
		}*/
		
		
		
		
		return invoices;
	}

	private void processInvoicePerProject(Project p, Client c, Date invoiceDate, Invoice lastInvoice) {

		Invoice invoice = new Invoice();
		Set<InvoiceLineItems> invoiceLis = new HashSet<InvoiceLineItems>();
		for (Employee e : p.getEmployee()) {
			//if the employeeid or projectid is not valid dont run the remaining part of the method
			if(e.getId() == null || e.getId() < 0 || p.getId() == null || p.getId() <0 )
				continue;
			List<InvoiceLineItems> lineItems = invoiceLineItemsDao.listNotInvoiceGeratedAllByProjectId(p, e);
			List<InvoiceLineItems> lineItemsUpdated = new ArrayList<>();
			if(lineItems == null)
				continue;
			for (InvoiceLineItems lts : lineItems) {
				//InvoiceLineItems lineItem = new InvoiceLineItems(lts);
				///////lts.setStatus(InvoiceLineItemStatus.INVOICE_GENERATED);
				//invoiceLineItemsDao.update(lineItem);
				//lineItemsUpdated.add(lineItem);
				
				//////toUpdate.add(lts);
				lineItemsUpdated.add(lts);
			}
			invoiceLis.addAll(lineItemsUpdated);
		}
		if (invoiceDate == null)
			invoiceDate = p.getStartDate();
		invoice.setInvoiceLineItems(invoiceLis);
		invoice.setClient(c);
		invoice.setDate(invoiceDate);
		//invoiceDao.update(invoice);
		if(lastInvoice == null)
			invoice.setNumber("1");
		else{
			int i = Integer.parseInt(lastInvoice.getNumber()) + 1;
		    invoice.setNumber(String.valueOf(i));
		}
		
		
		
		invoiceDao.create(invoice);
		
		/*toCreate.add(invoice);

		generatePaperInvoice(invoice);

		invoices.add(invoice);*/
		
	}

	private void generatePaperInvoice(Invoice invoice) {

	}

	@Inject
	ProjectDao projectDao;

	private void addInvoiceLineItems(InvoiceFreq fre) {
		// 
		List<Client> cs = clientDao.getAll();
		Set<Employee> es;
		Set<Project> ps;
		
		List<InvoiceLineItems> toinsert = new ArrayList<InvoiceLineItems>();
		
		for (Client c : cs) {

			if (c.getInvoiceFreq() == fre) {
				ps = c.getProjects();
				for (Project p : ps) {
					if (!isInvalidBillingRange(fre, p, c))
						continue;
					es = p.getEmployee();
					for (Employee e : es) {
						InvoiceLineItems lt = getEmployeeApprovedProjectHours(e, p);
						toinsert.add(lt);
						//
					}

				}

			}
		}
		
		
		
		for(InvoiceLineItems l : toinsert){
			invoiceLineItemsDao.update(l);
		}

	}

	private boolean isInvalidBillingRange(InvoiceFreq fre, Project p, Client c) {
		Date lastBillDate;
		Invoice previousInvoice = invoiceDao.lastInvoiceByClient(c);
		if (previousInvoice == null)
			lastBillDate = p.getStartDate();
		else
			lastBillDate = previousInvoice.getDate();
		date = getCurrentBillingDate(fre, lastBillDate);
		if (date == null || date.after(new Date()))
			return false;
		else
			return true;
	}

	

	private InvoiceLineItems getEmployeeApprovedProjectHours(Employee e, Project p) {
		float hours = 0;
		double amount = 0;
		List<NewTimeEntry> timeEntry = newTimeEntryDao.listOfAllTimeEntry();
		InvoiceLineItems lineItem = new InvoiceLineItems();

		Date startDate = new Date();
		Date endDate = new Date();
		int y = 0;

		for (NewTimeEntry t : timeEntry) {
			if (t.getStatus() == Status.APPROVED && t.getStatus()!= Status.INVOICED && t.getEmployee().getId() == e.getId()
					&& t.getProject().getId() == p.getId()) {
				hours += t.getTotalHours();
				t.setStatus(Status.INVOICED);
				newTimeEntryDao.update(t);

				if (y == 0) {
					startDate = t.getStartDate();
					endDate = t.getEndDate();
				}
				if (t.getStartDate().before(startDate))
					startDate = t.getStartDate();
				if (t.getEndDate().after(endDate))
					endDate = t.getEndDate();

				y++;
			}

		}

		amount = e.getRate() * hours;

		lineItem.setHours(hours);
		lineItem.setStartDate(startDate);
		lineItem.setProject(p);
		lineItem.setAmount(amount);
		lineItem.setEmployee(e);
		lineItem.setStatus(InvoiceLineItemStatus.INVOICE_NOT_GENERATED);

		return lineItem;
	}

	@SuppressWarnings("deprecation")
	public Date getCurrentBillingDate(InvoiceFreq enu, Date lastBilledDate) {

		Calendar c = Calendar.getInstance();

		c.setTime(lastBilledDate);

		switch (enu) {

		case WEELLY: {

			c.add(Calendar.DAY_OF_WEEK, 7 - lastBilledDate.getDay());
			break;

		}

		case BIWEEKLY: {
			c.add(Calendar.DAY_OF_WEEK, 7 - lastBilledDate.getDay());
			c.add(Calendar.DAY_OF_WEEK, 7);
			break;

		}

		case MONTHLY: {

			c.add(Calendar.DAY_OF_WEEK, 7 - lastBilledDate.getDay());
			c.add(Calendar.DAY_OF_WEEK, 21);
			break;
		}
		case MONTHLY_CAL: {
			c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
			break;
		}
		}

		return c.getTime();

	}
	
	public void test(){
		
	}

}