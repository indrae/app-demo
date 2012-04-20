package com.app.demo.web.bean;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import com.app.demo.domain.Role;


@FacesConverter(value = "roleConverter")
public class RoleConverter implements Converter {

    public static List<Role> roles;
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
        if (submittedValue.trim().equals("")) {
            return null;
        } else {
            try {
                int number = Integer.parseInt(submittedValue);
                for (Role p : roles) {
                    if (p.getId() == number) {
                    	System.out.println("RoleConverter.getAsObject() : trace here ...");
                        return (Object)p;
                    }
                }
			} catch (NumberFormatException exception) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Conversion Error",
						"Not a valid role"));
			}
        }
        return null;
    }

    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            return String.valueOf(((Role) value).getId());
        }
    }
}
                