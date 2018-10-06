package com.org.entity;
import java.util.Date;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import com.org.domain.LogUser;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findMeasurementSheetSharedsByMeasurementSheet", "findMeasurementSheetSharedsBySharedBy", "findMeasurementSheetSharedsBySharedWith" })
public class MeasurementSheetShared {

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date sharedDate;

    
    @ManyToOne
    private LogUser sharedWith;

    @ManyToOne(optional = true)
    private MeasurementSheet measurementSheet;

    private boolean opened;

    @ManyToOne
    private LogUser sharedBy;

    
    @PrePersist
    protected void onCreate() {
        //this.setShare
        this.setSharedBy(LogUser.getCurrentUser());
    }
    
    public String getInboxTitle() {
    	return this.getMeasurementSheet().getAggreement().getAggreementNum()+"-"+this.getMeasurementSheet().getSerialNumberDisplayFormat()+":"+this.getSharedBy().getFullName()+" shared a measurement sheet with you on "+this.getSharedDate();
    }
    
    public String getOutboxTitle() {
    	return "you shared a measurement sheet '"+this.getMeasurementSheet().getAggreement().getAggreementNum()+"-"+this.getMeasurementSheet().getSerialNumberDisplayFormat()+"' with "+this.getSharedWith().getFullName()+" on "+this.getSharedDate();
    }
    
    public String getDownloadLink() {
    	return this.getMeasurementSheet().getDownloadLink();
    }
}
