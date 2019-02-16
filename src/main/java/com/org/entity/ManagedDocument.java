package com.org.entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.web.multipart.MultipartFile;
import com.org.constants.ManagedDocumentType;
import com.org.domain.LogUser;
import com.org.util.FileStorageProperties;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findManagedDocumentsByLogUser", "findManagedDocumentsByAggreement", "findManagedDocumentsByAggreementAndLogUser", "findManagedDocumentsByIdAndLogUser", "findManagedDocumentsByLogUserAndParent", "findManagedDocumentsByLogUserAndParentIsNull", "findManagedDocumentsByLogUserAndType" })
public class ManagedDocument {

    @Size(max = 500)
    private String description;

    @Transient
    //@NotNull
    private MultipartFile content;

    @NotNull
    private long fileSize;

    @Size(max = 200)
    private String url;

    @ManyToOne(optional = true)
    private Aggreement aggreement;

    @ManyToOne(optional = true)
    private ManagedDocument parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ManagedDocument> children = new ArrayList<ManagedDocument>();

    private ManagedDocumentType type;

    @ManyToOne
    private LogUser logUser;

    public String getStorageUrl() {
        String agg_num = aggreement == null ? "NA" : aggreement.getId().toString();
        String fileName = content.getSize() == 0 ? description : content.getOriginalFilename();
        return FileStorageProperties.MANAGED_DOCUMENT_FOLDER + "MANAGED_DOCUMENTS_" + agg_num + "_" + getId() + "_" + fileName;
    }

    public String getDownloadLink() {
        return "/manageddocuments/download?file=" + url;
    }

    @PrePersist
    public void prePersist() {
        setLogUser(LogUser.getCurrentUser());
    }

    public static Long getStorageByUser(long id) {
        TypedQuery q = ManagedDocument.entityManager().createQuery("SELECT SUM(o.fileSize) FROM ManagedDocument AS o WHERE o.logUser.id = :id", Long.class);
        q.setParameter("id", id);
        System.out.println("getting storage DAO");
        Long result = (Long) q.getSingleResult();
        return result == null ? 0 : ((Long) q.getSingleResult());
    }
}
