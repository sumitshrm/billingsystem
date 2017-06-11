// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.entity.Item;
import com.org.entity.ItemAbstract;
import com.org.entity.MeasurementSheet;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect ItemAbstract_Roo_Finder {
    
    public static Long ItemAbstract.countFindItemAbstractsByItem(Item item) {
        if (item == null) throw new IllegalArgumentException("The item argument is required");
        EntityManager em = ItemAbstract.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItemAbstract AS o WHERE o.item = :item", Long.class);
        q.setParameter("item", item);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ItemAbstract.countFindItemAbstractsByMeasurementSheet(MeasurementSheet measurementSheet) {
        if (measurementSheet == null) throw new IllegalArgumentException("The measurementSheet argument is required");
        EntityManager em = ItemAbstract.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItemAbstract AS o WHERE o.measurementSheet = :measurementSheet", Long.class);
        q.setParameter("measurementSheet", measurementSheet);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ItemAbstract.countFindItemAbstractsByMeasurementSheetAndItem(MeasurementSheet measurementSheet, Item item) {
        if (measurementSheet == null) throw new IllegalArgumentException("The measurementSheet argument is required");
        if (item == null) throw new IllegalArgumentException("The item argument is required");
        EntityManager em = ItemAbstract.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItemAbstract AS o WHERE o.measurementSheet = :measurementSheet AND o.item = :item", Long.class);
        q.setParameter("measurementSheet", measurementSheet);
        q.setParameter("item", item);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<ItemAbstract> ItemAbstract.findItemAbstractsByItem(Item item) {
        if (item == null) throw new IllegalArgumentException("The item argument is required");
        EntityManager em = ItemAbstract.entityManager();
        TypedQuery<ItemAbstract> q = em.createQuery("SELECT o FROM ItemAbstract AS o WHERE o.item = :item", ItemAbstract.class);
        q.setParameter("item", item);
        return q;
    }
    
    public static TypedQuery<ItemAbstract> ItemAbstract.findItemAbstractsByItem(Item item, String sortFieldName, String sortOrder) {
        if (item == null) throw new IllegalArgumentException("The item argument is required");
        EntityManager em = ItemAbstract.entityManager();
        String jpaQuery = "SELECT o FROM ItemAbstract AS o WHERE o.item = :item";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        TypedQuery<ItemAbstract> q = em.createQuery(jpaQuery, ItemAbstract.class);
        q.setParameter("item", item);
        return q;
    }
    
    public static TypedQuery<ItemAbstract> ItemAbstract.findItemAbstractsByMeasurementSheet(MeasurementSheet measurementSheet) {
        if (measurementSheet == null) throw new IllegalArgumentException("The measurementSheet argument is required");
        EntityManager em = ItemAbstract.entityManager();
        TypedQuery<ItemAbstract> q = em.createQuery("SELECT o FROM ItemAbstract AS o WHERE o.measurementSheet = :measurementSheet", ItemAbstract.class);
        q.setParameter("measurementSheet", measurementSheet);
        return q;
    }
    
    public static TypedQuery<ItemAbstract> ItemAbstract.findItemAbstractsByMeasurementSheet(MeasurementSheet measurementSheet, String sortFieldName, String sortOrder) {
        if (measurementSheet == null) throw new IllegalArgumentException("The measurementSheet argument is required");
        EntityManager em = ItemAbstract.entityManager();
        String jpaQuery = "SELECT o FROM ItemAbstract AS o WHERE o.measurementSheet = :measurementSheet";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        TypedQuery<ItemAbstract> q = em.createQuery(jpaQuery, ItemAbstract.class);
        q.setParameter("measurementSheet", measurementSheet);
        return q;
    }
    
    public static TypedQuery<ItemAbstract> ItemAbstract.findItemAbstractsByMeasurementSheetAndItem(MeasurementSheet measurementSheet, Item item) {
        if (measurementSheet == null) throw new IllegalArgumentException("The measurementSheet argument is required");
        if (item == null) throw new IllegalArgumentException("The item argument is required");
        EntityManager em = ItemAbstract.entityManager();
        TypedQuery<ItemAbstract> q = em.createQuery("SELECT o FROM ItemAbstract AS o WHERE o.measurementSheet = :measurementSheet AND o.item = :item", ItemAbstract.class);
        q.setParameter("measurementSheet", measurementSheet);
        q.setParameter("item", item);
        return q;
    }
    
    public static TypedQuery<ItemAbstract> ItemAbstract.findItemAbstractsByMeasurementSheetAndItem(MeasurementSheet measurementSheet, Item item, String sortFieldName, String sortOrder) {
        if (measurementSheet == null) throw new IllegalArgumentException("The measurementSheet argument is required");
        if (item == null) throw new IllegalArgumentException("The item argument is required");
        EntityManager em = ItemAbstract.entityManager();
        String jpaQuery = "SELECT o FROM ItemAbstract AS o WHERE o.measurementSheet = :measurementSheet AND o.item = :item";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        TypedQuery<ItemAbstract> q = em.createQuery(jpaQuery, ItemAbstract.class);
        q.setParameter("measurementSheet", measurementSheet);
        q.setParameter("item", item);
        return q;
    }
    
}
