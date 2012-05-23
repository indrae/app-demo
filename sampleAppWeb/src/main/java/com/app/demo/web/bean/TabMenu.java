package com.app.demo.web.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class TabMenu implements Serializable {

	private static final long serialVersionUID = -2494713580633091828L;
	private  int tabIndex = 0;
	
	private static String PAGE_USER = "/pages/users.xhtml?faces-redirect=true";
	private static String PAGE_PROFILE = "/pages/profile/profile.xhtml?faces-redirect=true";
	private static String PAGE_PASSWORD = "/pages/profile/password.xhtml?faces-redirect=true";
	

	public TabMenu() {
	}
	public int getTabIndex() {
		return tabIndex;
	}
	public void setTabIndex(int tabIndex) {
		this.tabIndex = tabIndex;
	}

	public String users() {
		setTabIndex(0);
		return PAGE_USER;
	}

	public String profile() {
		setTabIndex(1);
		return PAGE_PROFILE;
	}

	public String password() {
		setTabIndex(1);
		return PAGE_PASSWORD;
	}

}
