package com.app.demo.web.bean.support.util;

import java.util.HashMap;
import java.util.Map;

import com.app.demo.domain.Person;

public class TestRun {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("firstName", "kvic");
		filters.put("lastName", "kaizen");
		filters.put("username", "kvickaizen");
		Person p = new Person();
		p = (Person) ObjectMappingUtil.mappingDataObject(p, filters);
		System.out.println(p.toString());
	}

}
