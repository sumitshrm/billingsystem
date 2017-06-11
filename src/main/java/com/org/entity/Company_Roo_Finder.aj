// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.domain.LogUser;
import com.org.entity.Company;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Company_Roo_Finder {
    
    public static Long Company.countFindCompanysByCreatedBy(LogUser createdBy) {
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = Company.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Company AS o WHERE o.createdBy = :createdBy", Long.class);
        q.setParameter("createdBy", createdBy);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Company.countFindCompanysByNameLikeAndCreatedBy(String name, LogUser createdBy) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = Company.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Company AS o WHERE LOWER(o.name) LIKE LOWER(:name)  AND o.createdBy = :createdBy", Long.class);
        q.setParameter("name", name);
        q.setParameter("createdBy", createdBy);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<Company> Company.findCompanysByCreatedBy(LogUser createdBy) {
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = Company.entityManager();
        TypedQuery<Company> q = em.createQuery("SELECT o FROM Company AS o WHERE o.createdBy = :createdBy", Company.class);
        q.setParameter("createdBy", createdBy);
        return q;
    }
    
    public static TypedQuery<Company> Company.findCompanysByCreatedBy(LogUser createdBy, String sortFieldName, String sortOrder) {
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = Company.entityManager();
        String jpaQuery = "SELECT o FROM Company AS o WHERE o.createdBy = :createdBy";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        TypedQuery<Company> q = em.createQuery(jpaQuery, Company.class);
        q.setParameter("createdBy", createdBy);
        return q;
    }
    
    public static TypedQuery<Company> Company.findCompanysByNameLikeAndCreatedBy(String name, LogUser createdBy) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = Company.entityManager();
        TypedQuery<Company> q = em.createQuery("SELECT o FROM Company AS o WHERE LOWER(o.name) LIKE LOWER(:name)  AND o.createdBy = :createdBy", Company.class);
        q.setParameter("name", name);
        q.setParameter("createdBy", createdBy);
        return q;
    }
    
    public static TypedQuery<Company> Company.findCompanysByNameLikeAndCreatedBy(String name, LogUser createdBy, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        if (createdBy == null) throw new IllegalArgumentException("The createdBy argument is required");
        EntityManager em = Company.entityManager();
        String jpaQuery = "SELECT o FROM Company AS o WHERE LOWER(o.name) LIKE LOWER(:name)  AND o.createdBy = :createdBy";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        TypedQuery<Company> q = em.createQuery(jpaQuery, Company.class);
        q.setParameter("name", name);
        q.setParameter("createdBy", createdBy);
        return q;
    }
    
}
