package com.app.demo.dao.hibernate;

import java.util.Date;

public interface AuditableDate {

	public String getId();

	public void setId(String id);

	public Date getCreatedDate();

	public void setCreatedDate(Date createdDate);

	public Date getModifiedDate();

	public void setModifiedDate(Date modifiedDate);

}
