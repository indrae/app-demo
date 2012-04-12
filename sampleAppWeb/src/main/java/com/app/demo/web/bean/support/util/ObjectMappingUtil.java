package com.app.demo.web.bean.support.util;

import java.lang.reflect.Field;
import java.util.Map;

public class ObjectMappingUtil {

	/**
	 * Currently filter String and Boolean. Boolean will check if fieldName Class start with "is"
	 * @param obj
	 * @param filters
	 * @return
	 */
	public static Object mappingDataObject(Object obj,
			Map<String, String> filters) {
		if (obj == null)obj = new Object();
		try {
			for (String key : filters.keySet()) {
				String value = filters.get(key);
				try {
					Field f = obj.getClass().getDeclaredField(key);
					if (f.getName().equalsIgnoreCase(key)) {
						f.setAccessible(true);
						try {
							if(key.startsWith("is")){
								if(value.equalsIgnoreCase("Yes")) f.set(obj,Boolean.TRUE); 
								else if(value.equalsIgnoreCase("No")) f.set(obj,Boolean.FALSE);
							}else{		
								f.set(obj, value);
							}
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
		return obj;
	}
	

}
