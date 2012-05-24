package com.app.demo.web.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.app.demo.security.SpringSecurityContext;
import com.app.demo.domain.Person;
import com.app.demo.service.PersonService;

@ManagedBean
@SessionScoped
@Component
@Scope("session")
public class ProfileBean  implements Serializable{

	private static final long serialVersionUID = 8401513910942032783L;
	private Person person = new Person();
	private String currentPassword;
	private String newPassword;
	
	private static PersonService personService;
	
	
	@Autowired
	public ProfileBean(PersonService instance) {
		System.out.println("ProfileBean.ProfileBean()");
		if (personService == null) {
			personService = instance;
		}
		
		String userName = SpringSecurityContext
		.getUsername();
		if(person == null ) new Person();
		person.setUsername(userName);
		person = personService.getPersonWithAssociation(person);  
		setImage();
	}
	
	public void setImage (){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        if(person != null)
        	session.setAttribute("bytePhoto", Base64.decodeBase64(person.getPhoto64()));
        else
        	session.setAttribute("bytePhoto", Base64.decodeBase64((String)null));	
	}
	
	public void updateUser(ActionEvent actionEvent) {
		System.out.println("ProfileBean.updateUser()");
		try {
			person = personService.merge(person);
			FacesContext.getCurrentInstance().addMessage("formUser", new FacesMessage(FacesMessage.SEVERITY_INFO,"Update Successfull", "Your profile updated"));  
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("formUser", new FacesMessage(FacesMessage.SEVERITY_WARN,"Update Failed", "Your profile failed to update!"));   
		}
	}
	
	public void updatePassword(ActionEvent actionEvent) {
		System.out.println("ProfileBean.updatePassword()");
		if(person.getPassword().equals(currentPassword)){
			person.setPassword(newPassword);
			try {
				person = personService.merge(person);
				FacesContext.getCurrentInstance().addMessage("formUser", new FacesMessage(FacesMessage.SEVERITY_INFO,"Update Password Successfull", "Please re-login"));  
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage("formUser", new FacesMessage(FacesMessage.SEVERITY_WARN,"Update Password Failed", "Your password failed to update!"));   
			}
			
		}else{
			FacesContext.getCurrentInstance().addMessage("formUser", new FacesMessage(FacesMessage.SEVERITY_WARN,"Update Password Failed", "Your current password wrong!")); 
		}
	}
	
	public void handleFileUpload(FileUploadEvent event) {
        try {
            person.setPhoto64(Base64.encodeBase64String(event.getFile().getContents()));
            person = personService.merge(person);
        	FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");  
            FacesContext.getCurrentInstance().addMessage("formUser", msg); 

        } catch (Exception e) {
        	FacesMessage msg = new FacesMessage("Failed", event.getFile().getFileName() + "is failed to upload");  
            FacesContext.getCurrentInstance().addMessage("formUser", msg); 
        }
    } 
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	

}
