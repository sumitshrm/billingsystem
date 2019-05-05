// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.entity.Item;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Item_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Item.entityManager;
    
    public static final List<String> Item.fieldNames4OrderClauseFilter = java.util.Arrays.asList("itemNumber", "subItemNumber", "aggreement", "subItems", "sortOrder", "description", "unit", "quantityPerUnit", "isExtraItem", "fullRate", "partRate", "quantity", "drsCode", "dsrRate", "measurementSheetId", "itemAbstracts", "logUser", "fullDescription", "parentItemDescription", "delimiter", "seperator", "parentItem");
    
    public static final EntityManager Item.entityManager() {
        EntityManager em = new Item().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    @Transactional
    public static long Item.countItems() {
        return findAllItems().size();
    }
    
    @Transactional
    public static List<Item> Item.findAllItems() {
        return entityManager().createQuery("SELECT o FROM Item o", Item.class).getResultList();
    }
    
    @Transactional
    public static List<Item> Item.findAllItems(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Item o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Item.class).getResultList();
    }
    
    @Transactional
    public static Item Item.findItem(Long id) {
        if (id == null) return null;
        return entityManager().find(Item.class, id);
    }
    
    @Transactional
    public static List<Item> Item.findItemEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Item o", Item.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public static List<Item> Item.findItemEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Item o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Item.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void Item.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Item.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Item attached = Item.findItem(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Item.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Item.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Item Item.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Item merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
