// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.domain.LogUser;
import com.org.entity.LabourEntry;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect LabourEntry_Roo_Finder {
    
    public static Long LabourEntry.countFindLabourEntrysByCreatedBy(LogUser createdBy) {
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = LabourEntry.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LabourEntry AS o WHERE o.createdBy = :createdBy", Long.class);
        q.setParameter("createdBy", createdBy);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<LabourEntry> LabourEntry.findLabourEntrysByCreatedBy(LogUser createdBy) {
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = LabourEntry.entityManager();
        TypedQuery<LabourEntry> q = em.createQuery("SELECT o FROM LabourEntry AS o WHERE o.createdBy = :createdBy", LabourEntry.class);
        q.setParameter("createdBy", createdBy);
        return q;
    }
    
    public static TypedQuery<LabourEntry> LabourEntry.findLabourEntrysByCreatedBy(LogUser createdBy, String sortFieldName, String sortOrder) {
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = LabourEntry.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LabourEntry AS o WHERE o.createdBy = :createdBy");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LabourEntry> q = em.createQuery(queryBuilder.toString(), LabourEntry.class);
        q.setParameter("createdBy", createdBy);
        return q;
    }
    
}
