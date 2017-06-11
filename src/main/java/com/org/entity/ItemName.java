package com.org.entity;
import javax.persistence.ManyToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.org.domain.LogUser;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findItemNamesByCreatedBy", "findItemNamesByNameAndCreatedBy", "findItemNamesByNameLikeAndCreatedBy" })
public class ItemName extends ManagedEntity {

	public ItemName() {
		super();
	}
	
    public ItemName(String name) {
		super();
		this.name = name;
	}

	private String name;
}
