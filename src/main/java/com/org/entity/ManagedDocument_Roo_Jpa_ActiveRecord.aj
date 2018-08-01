// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.entity.ManagedDocument;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ManagedDocument_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager ManagedDocument.entityManager;
    
    public static final List<String> ManagedDocument.fieldNames4OrderClauseFilter = java.util.Arrays.asList("description", "content", "fileSize", "url", "aggreement", "logUser");
    
    public static final EntityManager ManagedDocument.entityManager() {
        EntityManager em = new ManagedDocument().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    @Transactional
    public static long ManagedDocument.countManagedDocuments() {
        return findAllManagedDocuments().size();
    }
    
    @Transactional
    public static List<ManagedDocument> ManagedDocument.findAllManagedDocuments() {
        return entityManager().createQuery("SELECT o FROM ManagedDocument o", ManagedDocument.class).getResultList();
    }
    
    @Transactional
    public static List<ManagedDocument> ManagedDocument.findAllManagedDocuments(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ManagedDocument o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ManagedDocument.class).getResultList();
    }
    
    @Transactional
    public static ManagedDocument ManagedDocument.findManagedDocument(Long id) {
        if (id == null) return null;
        return entityManager().find(ManagedDocument.class, id);
    }
    
    @Transactional
    public static List<ManagedDocument> ManagedDocument.findManagedDocumentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ManagedDocument o", ManagedDocument.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public static List<ManagedDocument> ManagedDocument.findManagedDocumentEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ManagedDocument o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ManagedDocument.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void ManagedDocument.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void ManagedDocument.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ManagedDocument attached = ManagedDocument.findManagedDocument(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void ManagedDocument.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void ManagedDocument.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public ManagedDocument ManagedDocument.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ManagedDocument merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}