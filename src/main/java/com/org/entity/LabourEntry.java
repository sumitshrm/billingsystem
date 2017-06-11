package com.org.entity;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findLabourEntrysByCreatedBy" })
public class LabourEntry extends ManagedEntity {

    @ManyToOne
    private Aggreement aggreement;

    @ManyToOne(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
    private LabourSupplier labourSupplier;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date date;

    private String location;

    private String natureOfWork;

    private int numberOfWorkers;

    private String unit;

    private double rate;

    @Size(max = 200)
    private String remarks;
    
    public double getAmount(){
    	return numberOfWorkers * rate;
    }
}
