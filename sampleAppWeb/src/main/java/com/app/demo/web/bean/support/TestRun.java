package com.app.demo.web.bean.support;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

import com.app.demo.domain.Person;

public class TestRun {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, String> filters = new HashMap<String, String>();
		filters.put("firstName", "mantap");
		filters.put("lastName", "kui kui");
		filters.put("username", "userwoi");
		try {
			Person p = new Person();
			for (String str : filters.keySet()) {
				try {
					Field f = p.getClass().getDeclaredField(str);
					if (f.getName().equalsIgnoreCase(str)) {
						f.setAccessible(true);
						try {
							f.set(p, filters.get(str));
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}

	}

}
