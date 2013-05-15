package com.app.demo.web.bean;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.commons.codec.binary.Base64;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.app.demo.domain.Civility;
import com.app.demo.domain.Person;
import com.app.demo.domain.Role;
import com.app.demo.domain.Civility;
import com.app.demo.repository.PersonRepository;
import com.app.demo.repository.RoleRepository;
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
	private Person personAdd = new Person();
	private Map<String, String> roles;
	private List<String> selectedRoles;
	private StreamedContent graphicPhoto; 
	private StreamedContent graphicPhotoAdd;
	private LazyDataModel<Person> lazyModel;
	private static PersonRepository personService;
	private static RoleRepository roleService;

	
	
	@Autowired
	public PersonBean(PersonRepository instance, RoleRepository instance2) {
		if (personService == null) {
			personService = instance;
		}
		if (roleService == null) {
			roleService = instance2;
		}

		if (roles == null) {
			List<Role> listRoles = roleService.find();

			roles = new HashMap<String, String>();
			for (Role role : listRoles) {
				roles.put(role.getRoleName(), role.getRoleName());
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
			per.setCivility(Civility.MR);
			per.setBirthDate(new Date());
			per.setFirstName("Sample" + i);
			per.setLastName("Last" + i);
			per.setPassword("sample");
			//personService.save(per); 

			per.addSecurityRole(role);
			personService.merge(per);

		}
		Long end = Calendar.getInstance().getTimeInMillis();
		Long totalTime = end - timeStart;
		System.out.println(" Total : " + totalTime);

	}

	public void onRowSelect(SelectEvent event) {
		setPerson((Person) event.getObject());
	}

	public void updateUser(ActionEvent actionEvent) {
		try {
			personService.save(person);
			person.setSecurityRoles(new ArrayList<Role>());
			for (String s : selectedRoles) {
				Role r = roleService.getByRoleName(s);
				person.addSecurityRole(r);
			}
			person = personService.merge(person);
			FacesContext.getCurrentInstance().addMessage(
					"form",
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Update Successful", "User "
									+ person.getUsername() + " updated"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					"form",
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Update Failed", "User " + person.getUsername()
									+ " failed to update!"));
		}
	}

	public void addUser(ActionEvent actionEvent) {
		try {
			PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		    String hashedPassword = passwordEncoder.encode(personAdd.getUsername());
			personAdd.setPassword(hashedPassword);
			personAdd.setIsEnabled(true);
			if(personService.getByUsername(personAdd.getUsername()) != null){
				FacesContext.getCurrentInstance().addMessage(
						"form",
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Register Failed", "User Name "
										+ personAdd.getUsername()
										+ " is duplicate!"));
			}
			else if(personService.getByEmail(personAdd.getEmail()) != null){
				FacesContext.getCurrentInstance().addMessage(
						"form",
						new FacesMessage(FacesMessage.SEVERITY_WARN,
								"Register Failed", "Email "
										+ personAdd.getEmail()
										+ " is duplicate!"));
			}else {
				personService.save(personAdd);
				for (String s : selectedRoles) {
					Role r = roleService.getByRoleName(s);
					if (!personAdd.containsSecurityRole(r))
						personAdd.addSecurityRole(r);
				}
				personService.merge(personAdd);
				FacesContext.getCurrentInstance()
						.addMessage(
								"form",
								new FacesMessage(FacesMessage.SEVERITY_INFO,
										"Register Successful", "User "
												+ personAdd.getUsername()
												+ " registered"));
				personAdd = new Person();
				selectedRoles = new ArrayList<String>();
		      }
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					"form",
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Register Failed", "User "
									+ personAdd.getUsername()
									+ " failed to register!"));
		}
	}

	public void handleFileUpload(FileUploadEvent event) {  
        BufferedImage readImage = null;
        try {
            // Read from an input stream
            InputStream is = event.getFile().getInputstream();
            readImage = ImageIO.read(is);
//            int h = readImage.getHeight();
//            int w = readImage.getWidth();  
//            if(h == 100 &&  w == 100){
            	FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");  
                FacesContext.getCurrentInstance().addMessage("form", msg); 
             
                if(event.getComponent().getClientId().equalsIgnoreCase("formAdd:uploadPhotoAdd")){
                	personAdd.setPhoto(Base64.encodeBase64String(event.getFile().getContents()));
                }
                else if(event.getComponent().getClientId().equalsIgnoreCase("form:uploadPhotoEdit")){
                	person.setPhoto(Base64.encodeBase64String(event.getFile().getContents()));
                }
                
//            }else{
//            	FacesMessage msg = new FacesMessage("Failed", event.getFile().getFileName() + "Height and width must be 100px.");  
//                FacesContext.getCurrentInstance().addMessage("form", msg); 
//            }
        } catch (IOException e) {
        	FacesMessage msg = new FacesMessage("Failed", event.getFile().getFileName() + "is failed to upload");  
            FacesContext.getCurrentInstance().addMessage("form", msg); 
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
		selectedRoles = new ArrayList<String>();
		if (person != null) {
			for (String s : person.getRoleNames()) {
				selectedRoles.add(s);
			}
		}
			
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

	public Person getPersonAdd() {
		return personAdd;
	}

	public void setPersonAdd(Person personAdd) {
		this.personAdd = personAdd;
	}
	
	public StreamedContent getGraphicPhoto() {
		setImage (person);
		return graphicPhoto;
	}
	
	public StreamedContent getGraphicPhotoAdd() {
		setImage (personAdd);
		return graphicPhoto;
	}
	
	public void setImage (Person person){
		try {
	  
			if(person.getPhoto() != null){
				byte[] photo = Base64.decodeBase64(person.getPhoto());
			
				ByteArrayInputStream is = new ByteArrayInputStream(photo) ;
				graphicPhoto = new DefaultStreamedContent(is,null,"photo");
			}else{
				ServletContext sc = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
				String filename = sc.getRealPath("/resources/images/nophoto.jpeg");

				File file = new File(filename); 
				graphicPhoto = new DefaultStreamedContent(new FileInputStream(file), "image/jpeg","nophoto");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
