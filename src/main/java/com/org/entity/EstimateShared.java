package com.org.entity;
import java.util.Date;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import com.org.domain.LogUser;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findEstimateSharedsByEstimate", "findEstimateSharedsBySharedBy", "findEstimateSharedsBySharedWith" })
public class EstimateShared {

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date sharedDate;

    @ManyToOne
    private LogUser sharedWith;

    @ManyToOne(optional = true)
    private Estimate estimate;

    private boolean opened;

    @ManyToOne
    private LogUser sharedBy;

    @PrePersist
    protected void onCreate() {
        //this.setShare
        this.setSharedBy(LogUser.getCurrentUser());
    }
    
    public String getInboxTitle() {
    	return this.getSharedBy().getFullName()+" shared an estimate '"+this.getEstimate().getNameOfWork()+"' with you on "+this.getSharedDate();
    }
    
    public String getOutboxTitle() {
    	return "you shared an estimate '"+this.getEstimate().getNameOfWork()+"' with "+this.getSharedWith().getFullName()+" on "+this.getSharedDate();
    }
    
    public String getDownloadLink() {
    	return this.getEstimate().getDownloadLink();
    }
}
