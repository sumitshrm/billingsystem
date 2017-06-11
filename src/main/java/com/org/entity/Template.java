package com.org.entity;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import com.org.excel.util.TemplateType;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findTemplatesByType" })
public class Template implements IDocument{

    /**
     */
    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private TemplateType type;

    /**
     */
    @Size(max = 500)
    private String description;

    @NotNull
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] content;

    /**
     */
    @NotNull
    private String contentType;

    /**
     */
    private long fileSize;

    @Size(max = 200)
    private String url;
    
    public String getFileName(){
    	return name+".xlsm";
    }
}
