package com.app.demo.web.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
	List<Person> persons = new ArrayList<Person>();
	Person person = new Person();
	private LazyDataModel<Person> lazyModel; 
	
	private PersonService personService;
	private RoleService roleService;

	@Autowired
	public PersonBean(PersonService instance,RoleService roleService) {
		if (personService == null) {
			personService = instance;
		}
		if (this.roleService == null) {
			this.roleService = roleService;
		}
		
		lazyModel = new LazyPersonDataModel(persons,personService);
		//testInsert10k();
	}

	public void testInsert10k(){
		System.out.println("PersonBean.testInsert10k()");
		Long timeStart = Calendar.getInstance().getTimeInMillis();
			
			Role role = roleService.getByRoleName("ROLE_USER");
	    	for (int i = 10501; i <= 500000; i++) {
	    		Person per = new Person();
	    		per.setUsername("sampleUser"+i);
	    		per.setEmail(ValueGenerator.getUniqueEmail());
	    		per.setIsEnabled(true);
	    		per.setCivility(CivilityEnum.MR);
	    		per.setBirthDate(new Date());
	    		per.setFirstName("Sample"+i);
	    		per.setLastName("Last"+i);
	    		per.setPassword("sample");
	    		personService.save(per);
	    		
	    		per.addRole(role);
	    		personService.merge(per);
	    		
			}
	    	Long end = Calendar.getInstance().getTimeInMillis();
	    	Long totalTime = end - timeStart;
	    	System.out.println(" Total : "+totalTime);

		
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
	
	
}
