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
@RooJpaActiveRecord(finders = { "findManagedDocumentSharedsByManagedDocument", "findManagedDocumentSharedsBySharedBy", "findManagedDocumentSharedsBySharedWith" })
public class ManagedDocumentShared {

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date sharedDate;

    @ManyToOne
    private LogUser sharedWith;

    @ManyToOne(optional = true)
    private ManagedDocument managedDocument;

    private boolean opened;

    @ManyToOne
    private LogUser sharedBy;

    @PrePersist
    protected void onCreate() {
        //this.setShare
        this.setSharedBy(LogUser.getCurrentUser());
    }
    
    public String getInboxTitle() {
    	return this.getSharedBy().getFullName()+" shared a document '"+this.getManagedDocument().getDescription()+"' with you on "+this.getSharedDate();
    }
    
    public String getOutboxTitle() {
    	return "you shared a document'"+this.getManagedDocument().getDescription()+"' with "+this.getSharedWith().getFullName()+" on "+this.getSharedDate();
    }
    
    public String getDownloadLink() {
    	return this.getManagedDocument().getDownloadLink();
    }
}
