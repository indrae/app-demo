package com.app.demo.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

public class Default {

	private Integer version;
	private Date createdDate; // not null
	private String createdBy; // not null
	private Date modifiedDate; // not null
	private String modifiedBy; // not null

	// -- [version] ------------------------

	@Column(name = "VERSION", precision = 10)
	@Version
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	// -- [version] ------------------------
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE",length = 19,nullable = false )
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	// -- [version] ------------------------

	@Column(name = "CREATED_BY" , length = 32)
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	// -- [version] ------------------------

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE", length = 19,nullable = false )
	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	// -- [version] ------------------------

	@Column(name = "MODIFIED_BY" , length = 32)
	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	

}
