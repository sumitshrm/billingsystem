// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.entity.Aggreement;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Aggreement_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Aggreement.entityManager;
    
    public static final List<String> Aggreement.fieldNames4OrderClauseFilter = java.util.Arrays.asList("division", "subDivision", "description", "nameOfWork", "agency", "aggreementNum", "dateOfStart", "dateOfCompletionS", "dateOfCompletionA", "dateOfAbstract", "clausePercentage", "costIndex", "tenderCost", "estimatedCost", "clause", "items", "measurementSheets", "logUser");
    
    public static final EntityManager Aggreement.entityManager() {
        EntityManager em = new Aggreement().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    @Transactional
    public static long Aggreement.countAggreements() {
        return findAllAggreements().size();
    }
    
    @Transactional
    public static List<Aggreement> Aggreement.findAllAggreements() {
        return entityManager().createQuery("SELECT o FROM Aggreement o", Aggreement.class).getResultList();
    }
    
    @Transactional
    public static List<Aggreement> Aggreement.findAllAggreements(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Aggreement o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Aggreement.class).getResultList();
    }
    
    @Transactional
    public static Aggreement Aggreement.findAggreement(Long id) {
        if (id == null) return null;
        return entityManager().find(Aggreement.class, id);
    }
    
    @Transactional
    public static List<Aggreement> Aggreement.findAggreementEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Aggreement o", Aggreement.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public static List<Aggreement> Aggreement.findAggreementEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Aggreement o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Aggreement.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void Aggreement.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Aggreement.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Aggreement attached = Aggreement.findAggreement(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Aggreement.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Aggreement.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Aggreement Aggreement.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Aggreement merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
