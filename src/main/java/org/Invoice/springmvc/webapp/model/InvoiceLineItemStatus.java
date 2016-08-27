package org.Invoice.springmvc.webapp.model;
public enum InvoiceLineItemStatus {
	INVOICE_GENERATED(1), INVOICE_NOT_GENERATED(0);
	private final int value;

	InvoiceLineItemStatus(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
	
}