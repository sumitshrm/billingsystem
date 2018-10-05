package com.org.entity;
import java.util.Date;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import com.org.domain.LogUser;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findManagedDocumentSharedsByManagedDocument" })
public class ManagedDocumentShared {

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date sharedDate;

    @ManyToOne
    private LogUser sharedWith;

    @ManyToOne(optional = true)
    private ManagedDocument managedDocument;

    private boolean opened;
}
