package org.Invoice.util;

import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;

@FacesConverter("dateConverter")
public class DateConverter extends DateTimeConverter {
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !"".equals(value)) {
            //Validate the input is in one of two accepted forms, converting one form into the other if needed
            //One pattern is MMDDYYYY
            if (Pattern.matches("[0-9]{8}", value)) {
                value = value.substring(0,2) + "/" + value.substring(2,4) + "/" + value.substring(4);
            }
            //Other pattern is MM/DD/YYYY
            else if (Pattern.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}", value)) {
                //No conversion needed
            }
            //Neither pattern exists, tell the user the legal pattern
            else {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date input must either be in the form MMDDYYYY or MM/DD/YYYY.",
                        "Date input must either be in the form MMDDYYYY or MM/DD/YYYY."));
            }
        }
        return super.getAsObject(context, component, value);
    }
}