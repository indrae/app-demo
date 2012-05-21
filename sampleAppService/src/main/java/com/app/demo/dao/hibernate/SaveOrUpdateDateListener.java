package com.app.demo.dao.hibernate;

import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.def.DefaultSaveOrUpdateEventListener;

public class SaveOrUpdateDateListener extends DefaultSaveOrUpdateEventListener {

	private static final long serialVersionUID = 2443629268367827215L;
	static Logger logger = Logger.getLogger(SaveOrUpdateDateListener.class);

	@Override
	public void onSaveOrUpdate(SaveOrUpdateEvent event) {
		logger.debug("Entering onSaveOrUpdate()");

		if (event.getObject() instanceof AuditableDate) {
			AuditableDate record = (AuditableDate) event.getObject();

			// set the Updated date/time
			record.setModifiedDate(new Date());

			// set the Created date/time
			if (record.getId() == null) {
				record.setCreatedDate(new Date());
			}
		}
	}
}