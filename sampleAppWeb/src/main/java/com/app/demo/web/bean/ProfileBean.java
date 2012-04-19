package com.app.demo.web.bean;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.app.demo.security.SpringSecurityContext;
import com.app.demo.dao.support.SearchMode;
import com.app.demo.dao.support.SearchTemplate;
import com.app.demo.domain.Person;
import com.app.demo.domain.enums.CivilityEnum;
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
		if (personService == null) {
			personService = instance;
		}
		
		String userName = SpringSecurityContext
		.getUsername();
		person.setUsername(userName);
		person = personService.getPersonWithAssociation(person);  
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
