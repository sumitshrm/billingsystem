package com.org.entity;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import com.org.domain.LogUser;
import com.org.util.QueryUtil;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findMaterialEntrysByCreatedBy" })
public class MaterialEntry extends ManagedEntity {

    @ManyToOne
    private Aggreement aggreement;

    private String site;

    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
    private ItemName item;

    @NotNull
    private String descriptionOfItem1;

    private String descriptionOfItem2;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date date;

    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
    private Company company;

    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
    private Supplier supplier;

    private double quantity;

    private String unit;

    private double rate;

    @Size(max = 200)
    private String remarks;
    
    public double getAmount(){
    	return quantity * rate;
    }
    
}
