package com.app.demo.web.bean;

import java.util.Calendar;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class Common {
	private Date todayDate;

	public Date getTodayDate() {
		todayDate = Calendar.getInstance().getTime();
		return todayDate;
	}

	public void setTodayDate(Date todayDate) {
		this.todayDate = todayDate;
	}
}
