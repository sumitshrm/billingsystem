// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.domain.LogUser;
import com.org.entity.ItemName;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect ItemName_Roo_Finder {
    
    public static Long ItemName.countFindItemNamesByCreatedBy(LogUser createdBy) {
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = ItemName.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItemName AS o WHERE o.createdBy = :createdBy", Long.class);
        q.setParameter("createdBy", createdBy);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ItemName.countFindItemNamesByNameAndCreatedBy(String name, LogUser createdBy) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = ItemName.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItemName AS o WHERE o.name = :name AND o.createdBy = :createdBy", Long.class);
        q.setParameter("name", name);
        q.setParameter("createdBy", createdBy);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ItemName.countFindItemNamesByNameLikeAndCreatedBy(String name, LogUser createdBy) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = ItemName.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItemName AS o WHERE LOWER(o.name) LIKE LOWER(:name)  AND o.createdBy = :createdBy", Long.class);
        q.setParameter("name", name);
        q.setParameter("createdBy", createdBy);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<ItemName> ItemName.findItemNamesByCreatedBy(LogUser createdBy) {
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = ItemName.entityManager();
        TypedQuery<ItemName> q = em.createQuery("SELECT o FROM ItemName AS o WHERE o.createdBy = :createdBy", ItemName.class);
        q.setParameter("createdBy", createdBy);
        return q;
    }
    
    public static TypedQuery<ItemName> ItemName.findItemNamesByCreatedBy(LogUser createdBy, String sortFieldName, String sortOrder) {
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = ItemName.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ItemName AS o WHERE o.createdBy = :createdBy");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItemName> q = em.createQuery(queryBuilder.toString(), ItemName.class);
        q.setParameter("createdBy", createdBy);
        return q;
    }
    
    public static TypedQuery<ItemName> ItemName.findItemNamesByNameAndCreatedBy(String name, LogUser createdBy) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = ItemName.entityManager();
        TypedQuery<ItemName> q = em.createQuery("SELECT o FROM ItemName AS o WHERE o.name = :name AND o.createdBy = :createdBy", ItemName.class);
        q.setParameter("name", name);
        q.setParameter("createdBy", createdBy);
        return q;
    }
    
    public static TypedQuery<ItemName> ItemName.findItemNamesByNameAndCreatedBy(String name, LogUser createdBy, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = ItemName.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ItemName AS o WHERE o.name = :name AND o.createdBy = :createdBy");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItemName> q = em.createQuery(queryBuilder.toString(), ItemName.class);
        q.setParameter("name", name);
        q.setParameter("createdBy", createdBy);
        return q;
    }
    
    public static TypedQuery<ItemName> ItemName.findItemNamesByNameLikeAndCreatedBy(String name, LogUser createdBy) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = ItemName.entityManager();
        TypedQuery<ItemName> q = em.createQuery("SELECT o FROM ItemName AS o WHERE LOWER(o.name) LIKE LOWER(:name)  AND o.createdBy = :createdBy", ItemName.class);
        q.setParameter("name", name);
        q.setParameter("createdBy", createdBy);
        return q;
    }
    
    public static TypedQuery<ItemName> ItemName.findItemNamesByNameLikeAndCreatedBy(String name, LogUser createdBy, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = ItemName.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ItemName AS o WHERE LOWER(o.name) LIKE LOWER(:name)  AND o.createdBy = :createdBy");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItemName> q = em.createQuery(queryBuilder.toString(), ItemName.class);
        q.setParameter("name", name);
        q.setParameter("createdBy", createdBy);
        return q;
    }
    
}
