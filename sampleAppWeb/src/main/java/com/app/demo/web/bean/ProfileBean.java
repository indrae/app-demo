package com.app.demo.web.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.app.demo.security.SpringSecurityContext;
import com.app.demo.domain.Person;
import com.app.demo.domain.enums.CivilityEnum;
import com.app.demo.service.PersonService;

@ManagedBean
@SessionScoped
@Component
@Scope("session")
public class ProfileBean {
	Person person = new Person();

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
	
	public CivilityEnum[] getCivilityValues() {
	    return CivilityEnum.values();
   }

	public void updateUser(ActionEvent actionEvent) {
		System.out.println("ProfileBean.updateUser()");
		person = personService.merge(person);
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

}
