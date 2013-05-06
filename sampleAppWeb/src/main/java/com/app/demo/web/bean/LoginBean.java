package com.app.demo.web.bean;


import java.io.Serializable;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.app.demo.domain.Person;


@ManagedBean
@SessionScoped
@Component
public class LoginBean  implements Serializable {

	private static final long serialVersionUID = 2868742783741899100L;
	private Person person = new Person();
	private Boolean isAdmin = false;
	@NotEmpty
    @Size(min = 1, max = 25)
    private String userName;
	
    @NotEmpty
    @Size(min = 1, max = 25)
    private String password;

	

	@Resource(name = "authenticationManager")
	private AuthenticationManager am;
	
    public LoginBean() {

    }

    //ActionEvent actionEvent
	public String login() throws java.io.IOException {
		try {
            Authentication req = new UsernamePasswordAuthenticationToken(this.getUserName(), getPassword());
            Authentication result = am.authenticate(req);
            SecurityContextHolder.getContext().setAuthentication(result);
            person = (Person) SecurityContextHolder.getContext().getAuthentication().getDetails();
            System.out.println("Login Success! ..");
            
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            
            System.out.println("is Admin : "+request.isUserInRole("ROLE_ADMIN"));
            isAdmin = request.isUserInRole("ROLE_ADMIN");
            
            return "/pages/users?faces-redirect=true";
        } catch (AuthenticationException ex) {
        	System.out.println("Login Failed");
        	ex.printStackTrace();
        	FacesContext.getCurrentInstance().addMessage("formLogin", new FacesMessage(FacesMessage.SEVERITY_WARN,"Login Failed", "User Name and Password Not Match!"));  
             
            return "/login";
        }
	}

	public String logout() {
		//System.out.println("LoginBean.logout()....");
		SecurityContextHolder.getContext().setAuthentication(null);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.clear();
		return "/login";
	}
	
	public String getLogoutHidden() {
		//System.out.println("LoginBean.getLogoutHidden()....");
		SecurityContextHolder.getContext().setAuthentication(null);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.clear();
		return "logout";
	}
	
	public void setLogoutHidden(String logoutHidden) {
	}
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}


	
	
}