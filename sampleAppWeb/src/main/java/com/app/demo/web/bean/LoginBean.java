package com.app.demo.web.bean;


import java.io.Serializable;

import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@ManagedBean
@SessionScoped
@Component
public class LoginBean  implements Serializable {

	private static final long serialVersionUID = 2868742783741899100L;

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

	public String login() throws java.io.IOException {
		try {
            Authentication request = new UsernamePasswordAuthenticationToken(this.getUserName(), getPassword());
            Authentication result = am.authenticate(request);
            SecurityContextHolder.getContext().setAuthentication(result);
            System.out.println("OK");
            return "/pages/users";
        } catch (AuthenticationException ex) {
        	System.out.println("failed");
            //String loginFailedMessage = FacesUtils.getBundleKey("msg", "login.failed");
            //FacesUtils.addErrorMessage(loginFailedMessage);
            return "/login";
        }
	}
	
	public String logout() {
		System.out.println("LoginBean.logout()....");
		SecurityContextHolder.getContext().setAuthentication(null);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.clear();
		return "/login";
	}
	
	public void keepAlive(ActionEvent e){
		System.out.println("LoginBean.keepAlive()");
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
}