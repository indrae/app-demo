package com.app.demo.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.app.demo.domain.Person;
import com.app.demo.domain.Role;
import com.app.demo.domain.enums.CivilityEnum;
import com.app.demo.service.PersonService;
import com.app.demo.service.RoleService;
import com.app.demo.web.bean.support.LazyPersonDataModel;
import com.app.demo.web.test.ValueGenerator;

@ManagedBean
@SessionScoped
@Component
@Scope("session")

public class PersonBean implements Serializable {
	private static final long serialVersionUID = -3323403390205788011L;
	private List<Person> persons = new ArrayList<Person>();
	private Person person = new Person();
	private static Map<String,String> roles;  
	private List<String> selectedRoles;  
	private List<Role> listRoles ;
	private LazyDataModel<Person> lazyModel;

	private static PersonService personService;
	private static RoleService roleService;

	@Autowired
	public PersonBean(PersonService instance, RoleService instance2) {
		if (personService == null) {
			personService = instance;
		}
		if (roleService == null) {
			roleService = instance2;
		}
		
		if (roles == null) {
			listRoles = roleService.find();
			RoleConverter.roles = listRoles;
			
			roles =  new HashMap<String, String>();
			for (Role role : listRoles) {
				roles.put(role.getRoleName(), role.getRoleDesc());
			}
		}
		lazyModel = new LazyPersonDataModel(persons, personService);
		
		// testInsert();
	}

	public void testInsert() {
		System.out.println("PersonBean.testInsert10k()");
		Long timeStart = Calendar.getInstance().getTimeInMillis();

		Role role = roleService.getByRoleName("ROLE_USER");
		for (int i = 10501; i <= 500000; i++) {
			Person per = new Person();
			per.setUsername("sampleUser" + i);
			per.setEmail(ValueGenerator.getUniqueEmail());
			per.setIsEnabled(true);
			per.setCivility(CivilityEnum.MR);
			per.setBirthDate(new Date());
			per.setFirstName("Sample" + i);
			per.setLastName("Last" + i);
			per.setPassword("sample");
			personService.save(per);

			per.addRole(role);
			personService.merge(per);

		}
		Long end = Calendar.getInstance().getTimeInMillis();
		Long totalTime = end - timeStart;
		System.out.println(" Total : " + totalTime);

	}

	public void onRowSelect(SelectEvent event) {  
		setPerson((Person) event.getObject());
        /*FacesMessage msg = new FacesMessage("Person Selected", ((Person) event.getObject()).getUsername());  
  
        FacesContext.getCurrentInstance().addMessage(null, msg);  */
    }
	
	public void updateUser(ActionEvent actionEvent) {
		try {
			for (String s : person.getRoleNames()) {
				System.out.println(" role : "+s);
			}
			person = personService.merge(person);
			FacesContext.getCurrentInstance().addMessage("form", new FacesMessage(FacesMessage.SEVERITY_INFO,"Update Successfull", "User <b>"+person.getFirstName()+"</b> updated"));  
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage("form", new FacesMessage(FacesMessage.SEVERITY_WARN,"Update Failed", "User <b>"+person.getFirstName()+"</b> failed to update!"));   
		}	
	}
	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public LazyDataModel<Person> getLazyModel() {
		return lazyModel;
	}

	public Map<String, String> getRoles() {
		
		return roles;
	}

	public List<String> getSelectedRoles() {
		return selectedRoles;
	}

	public void setSelectedRoles(List<String> selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

	public List<Role> getListRoles() {
		return listRoles;
	}

	public void setListRoles(List<Role> listRoles) {
		this.listRoles = listRoles;
	}


}
