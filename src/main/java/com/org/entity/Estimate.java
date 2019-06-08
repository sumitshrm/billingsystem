package com.org.entity;
import java.util.List;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.org.domain.LogUser;

@RooJavaBean
@RooToString
@JsonIgnoreProperties(ignoreUnknown = true)
@RooJpaActiveRecord(finders = { "findEstimatesByLogUser", "findEstimatesByIdAndLogUser" })
public class Estimate {

    private String nameOfWork;

    @Transient
    private MultipartFile content;

    private String url;

    @Transient
    private List<EstimateItem> items;

    @ManyToOne
    private LogUser logUser;

    @PrePersist
    public void prePersist() {
        setLogUser(LogUser.getCurrentUser());
    }

    public String getDownloadLink() {
        return "/manageddocuments/download?file=" + url;
    }
}
