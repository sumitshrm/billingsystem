// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.entity.Supplier;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Supplier_Roo_Jpa_ActiveRecord {
    
    public static final List<String> Supplier.fieldNames4OrderClauseFilter = java.util.Arrays.asList("name");
    
    @Transactional
    public static long Supplier.countSuppliers() {
        return findAllSuppliers().size();
    }
    
    @Transactional
    public static List<Supplier> Supplier.findAllSuppliers() {
        return entityManager().createQuery("SELECT o FROM Supplier o", Supplier.class).getResultList();
    }
    
    @Transactional
    public static List<Supplier> Supplier.findAllSuppliers(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Supplier o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Supplier.class).getResultList();
    }
    
    @Transactional
    public static Supplier Supplier.findSupplier(Long id) {
        if (id == null) return null;
        return entityManager().find(Supplier.class, id);
    }
    
    @Transactional
    public static List<Supplier> Supplier.findSupplierEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Supplier o", Supplier.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public static List<Supplier> Supplier.findSupplierEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Supplier o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Supplier.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public Supplier Supplier.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Supplier merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
