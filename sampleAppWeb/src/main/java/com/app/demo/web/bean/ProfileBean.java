package com.app.demo.web.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.dom4j.DocumentException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.demo.context.UserContext;
import com.app.demo.domain.Person;
import com.app.demo.repository.PersonRepository;
import java.awt.Graphics2D;  
import java.awt.image.BufferedImage;  
import java.io.ByteArrayInputStream;  
import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.IOException;

import javax.imageio.ImageIO;  
 
import org.primefaces.model.DefaultStreamedContent;   

@ManagedBean
@SessionScoped
@Component
@Scope("session")
public class ProfileBean  implements Serializable{

	private static final long serialVersionUID = 8401513910942032783L;
	private Person person = new Person();
	private String currentPassword;
	private String newPassword;
	private StreamedContent graphicPhoto; 
	
	private static PersonRepository personService;
	
	
	@Autowired
	public ProfileBean(PersonRepository instance) {
		System.out.println("ProfileBean.ProfileBean()");
		if (personService == null) {
			personService = instance;
		}
		
		String userName = UserContext.getUsername();
		person = personService.getByUsername(userName);
	}
	
	public void setImage (){
		try {
			 //Graphic Text  
			
			byte[] photo = Base64.decodeBase64(person.getPhoto());
			
			 ByteArrayInputStream is = new ByteArrayInputStream(photo) ;
			 //graphicPhoto = new DefaultStreamedContent(is, "application/pdf", "downloaded_primefaces.pdf");
			 graphicPhoto = new DefaultStreamedContent(is,null,"photo");
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateUser(ActionEvent actionEvent) {
		System.out.println("ProfileBean.updateUser()");
		try {
			person = personService.merge(person);
			FacesContext.getCurrentInstance().addMessage("formUser", new FacesMessage(FacesMessage.SEVERITY_INFO,"Update Successful", "Your profile updated"));  
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
				FacesContext.getCurrentInstance().addMessage("formUser", new FacesMessage(FacesMessage.SEVERITY_INFO,"Update Password Successful", "Please re-login"));  
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage("formUser", new FacesMessage(FacesMessage.SEVERITY_WARN,"Update Password Failed", "Your password failed to update!"));   
			}
			
		}else{
			FacesContext.getCurrentInstance().addMessage("formUser", new FacesMessage(FacesMessage.SEVERITY_WARN,"Update Password Failed", "Your current password wrong!")); 
		}
	}
	
	public void handleFileUpload(FileUploadEvent event) {
        try {
            person.setPhoto(Base64.encodeBase64String(event.getFile().getContents()));
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

	public StreamedContent getGraphicPhoto() {
		setImage ();
		return graphicPhoto;
	}

	

}
