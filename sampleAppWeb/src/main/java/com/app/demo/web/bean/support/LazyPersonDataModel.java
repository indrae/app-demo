package com.app.demo.web.bean.support;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.app.demo.dao.support.OrderBy;
import com.app.demo.dao.support.OrderByDirection;

import com.app.demo.dao.support.SearchParameters;
import com.app.demo.domain.Person;
import com.app.demo.repository.PersonRepository;

import com.app.demo.web.bean.support.util.ObjectMappingUtil;

/**
 * This can make it as Dynamic LazyDataModel. For this moment, create it for Person.
 * Easy more understanding.
 */

public class LazyPersonDataModel extends LazyDataModel<Person> {

	private static final long serialVersionUID = -27891531159872027L;
	private List<Person> datasource;
	
	private PersonRepository personService;

    public LazyPersonDataModel(List<Person> datasource,PersonRepository instance) {
		if (personService == null) {
			personService = instance;
		}
        this.datasource = datasource;
    }
    
    @Override
    public Person getRowData(String rowKey) {
        for(Person person : datasource) {
            if(person.getUsername().equals(rowKey))
                return person;
        }

        return null;
    }

    @Override
    public Object getRowKey(Person person) {
        return person.getUsername();
    }

    @Override
    public List<Person> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,String> filters) {
    	//System.out.println("LazyPersonDataModel.load()");
        SearchParameters searchTemplate = new SearchParameters();
        Person p = new Person();
        if (sortField != null && sortOrder != null) {
        	if(SortOrder.ASCENDING.equals(sortOrder))
        		searchTemplate.addOrderBy(new OrderBy(sortField, OrderByDirection.ASC));
        	else
        		searchTemplate.addOrderBy(new OrderBy(sortField, OrderByDirection.DESC));
        }else {
        	//searchTemplate.addOrderBy(new OrderBy(sortField, OrderByDirection.DESC));
        }
        
        //This will search using all
        if(filters.get("globalFilter") != null && filters.get("globalFilter").length() > 0){

        	p.setUsername(filters.get("globalFilter"));
        	p.setFirstName(filters.get("globalFilter"));
        	p.setEmail(filters.get("globalFilter"));
        	
        }else{
	        filters.remove("globalFilter");
	        p = (Person) ObjectMappingUtil.mappingDataObject(new Person(), filters);
        }
        searchTemplate.setFirstResult(first);
        searchTemplate.setMaxResults(first + pageSize);
        searchTemplate.anywhere();

        
        
        datasource = personService.find(p,searchTemplate);
        setRowCount(personService.findCount(p, searchTemplate));

        return datasource;
    }
}




                    