package com.app.demo.web.bean;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.app.demo.domain.Civility;

@ManagedBean
@SessionScoped
public class EnumObject implements Serializable{
	private static final long serialVersionUID = -6265265206049492852L;
	
	public EnumObject() {
	}

	public Civility[] getCivilityValues() {
	    return Civility.values();
   }
}
