package com.org.entity;
import java.util.Date;

import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.access.prepost.PreAuthorize;

import com.org.domain.LogUser;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(inheritanceType = "TABLE_PER_CLASS", mappedSuperclass=true)
public abstract class ManagedEntity {
	
	
	@ManyToOne
	private LogUser createdBy;
	
	@ManyToOne 
	private LogUser lastUpdatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
	private Date createdOn;
	
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
	private Date lastUpdatedOn;
	
	@PrePersist
	public void preSave(){
		this.setCreatedBy(LogUser.getCurrentUser());
		this.setLastUpdatedBy(LogUser.getCurrentUser());
		this.setCreatedOn(new Date());
		this.setLastUpdatedOn(new Date());
		beforeSave();
		
	}
	
	@PreUpdate
	public void preUpdate(){
		//this.setLastUpdatedBy(LogUser.getCurrentUser());
		this.setLastUpdatedOn(new Date());
		beforeUpdate();
		
	}
	
	protected void beforeSave(){
		
	}
	
	protected void beforeUpdate(){
		
	}
}
