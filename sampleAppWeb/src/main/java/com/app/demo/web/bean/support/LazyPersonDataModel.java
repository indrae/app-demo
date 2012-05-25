package com.app.demo.web.bean.support;

import java.util.List;
import java.util.Map;


import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.demo.dao.support.OrderBy;
import com.app.demo.dao.support.OrderByDirection;
import com.app.demo.dao.support.SearchMode;
import com.app.demo.dao.support.SearchTemplate;
import com.app.demo.domain.Person;
import com.app.demo.service.PersonService;

import com.app.demo.web.bean.support.util.ObjectMappingUtil;

/**
 * This can make it as Dynamic LazyDataModel. For this moment, create it for Person.
 * Easy more understanding.
 */

public class LazyPersonDataModel extends LazyDataModel<Person> {

	private static final long serialVersionUID = -27891531159872027L;
	private List<Person> datasource;
	
	private PersonService personService;

    public LazyPersonDataModel(List<Person> datasource,PersonService instance) {
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
        SearchTemplate searchTemplate = new SearchTemplate();
        
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
        	
        	Criterion crit1 = Restrictions.like("username", filters.get("globalFilter"),MatchMode.ANYWHERE);
        	Criterion crit2 = Restrictions.like("firstName", filters.get("globalFilter"),MatchMode.ANYWHERE);
        	Criterion crit3 = Restrictions.like("email", filters.get("globalFilter"),MatchMode.ANYWHERE);
            
        	Criterion orCrit1 = Restrictions.or(crit1, crit2);
            Criterion orCrit2 = Restrictions.or(orCrit1, crit3);
            
            searchTemplate.addCriterion(orCrit2);
        	
        }
        filters.remove("globalFilter");
        Person p = (Person) ObjectMappingUtil.mappingDataObject(new Person(), filters);

        searchTemplate.setFirstResult(first);
        searchTemplate.setMaxResults(first + pageSize);
        searchTemplate.setSearchMode(SearchMode.ANYWHERE);

        
        
        datasource = personService.findWithAssociation(p,searchTemplate);
        setRowCount(personService.findCount(p, searchTemplate));
       // datasource = personService.find(searchTemplate);
       // setRowCount(personService.findCount(searchTemplate));

        return datasource;
    }
}




                    