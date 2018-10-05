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
@RooJpaActiveRecord(finders = { "findEstimateSharedsByEstimate" })
public class EstimateShared {

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date sharedDate;

    @ManyToOne
    private LogUser sharedWith;

    @ManyToOne(optional = true)
    private Estimate estimate;

    private boolean opened;
}
