package com.org.entity;
import javax.persistence.ManyToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.org.domain.LogUser;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findCompanysByCreatedBy", "findCompanysByNameLikeAndCreatedBy" })
public class Company extends ManagedEntity {

	
    public Company() {
		super();
	}

	public Company(String name) {
		super();
		this.name = name;
	}

	private String name;
}
