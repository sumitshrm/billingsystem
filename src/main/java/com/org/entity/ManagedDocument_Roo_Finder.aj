// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.constants.ManagedDocumentType;
import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.ManagedDocument;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect ManagedDocument_Roo_Finder {
    
    public static Long ManagedDocument.countFindManagedDocumentsByAggreement(Aggreement aggreement) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        EntityManager em = ManagedDocument.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ManagedDocument AS o WHERE o.aggreement = :aggreement", Long.class);
        q.setParameter("aggreement", aggreement);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ManagedDocument.countFindManagedDocumentsByAggreementAndLogUser(Aggreement aggreement, LogUser logUser) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = ManagedDocument.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ManagedDocument AS o WHERE o.aggreement = :aggreement AND o.logUser = :logUser", Long.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("logUser", logUser);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ManagedDocument.countFindManagedDocumentsByIdAndLogUser(Long id, LogUser logUser) {
        if (id == null) throw new IllegalArgumentException("The id argument is required");
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = ManagedDocument.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ManagedDocument AS o WHERE o.id = :id AND o.logUser = :logUser", Long.class);
        q.setParameter("id", id);
        q.setParameter("logUser", logUser);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ManagedDocument.countFindManagedDocumentsByLogUser(LogUser logUser) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = ManagedDocument.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ManagedDocument AS o WHERE o.logUser = :logUser", Long.class);
        q.setParameter("logUser", logUser);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ManagedDocument.countFindManagedDocumentsByLogUserAndParent(LogUser logUser, ManagedDocument parent) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = ManagedDocument.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ManagedDocument AS o WHERE o.logUser = :logUser AND o.parent = :parent", Long.class);
        q.setParameter("logUser", logUser);
        q.setParameter("parent", parent);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ManagedDocument.countFindManagedDocumentsByLogUserAndParentIsNull(LogUser logUser) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = ManagedDocument.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ManagedDocument AS o WHERE o.logUser = :logUser AND o.parent IS NULL", Long.class);
        q.setParameter("logUser", logUser);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ManagedDocument.countFindManagedDocumentsByLogUserAndType(LogUser logUser, ManagedDocumentType type) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        if (type == null) throw new IllegalArgumentException("The type argument is required");
        EntityManager em = ManagedDocument.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ManagedDocument AS o WHERE o.logUser = :logUser AND o.type = :type", Long.class);
        q.setParameter("logUser", logUser);
        q.setParameter("type", type);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<ManagedDocument> ManagedDocument.findManagedDocumentsByAggreement(Aggreement aggreement) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        EntityManager em = ManagedDocument.entityManager();
        TypedQuery<ManagedDocument> q = em.createQuery("SELECT o FROM ManagedDocument AS o WHERE o.aggreement = :aggreement", ManagedDocument.class);
        q.setParameter("aggreement", aggreement);
        return q;
    }
    
    public static TypedQuery<ManagedDocument> ManagedDocument.findManagedDocumentsByAggreement(Aggreement aggreement, String sortFieldName, String sortOrder) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        EntityManager em = ManagedDocument.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ManagedDocument AS o WHERE o.aggreement = :aggreement");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ManagedDocument> q = em.createQuery(queryBuilder.toString(), ManagedDocument.class);
        q.setParameter("aggreement", aggreement);
        return q;
    }
    
    public static TypedQuery<ManagedDocument> ManagedDocument.findManagedDocumentsByAggreementAndLogUser(Aggreement aggreement, LogUser logUser) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = ManagedDocument.entityManager();
        TypedQuery<ManagedDocument> q = em.createQuery("SELECT o FROM ManagedDocument AS o WHERE o.aggreement = :aggreement AND o.logUser = :logUser", ManagedDocument.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("logUser", logUser);
        return q;
    }
    
    public static TypedQuery<ManagedDocument> ManagedDocument.findManagedDocumentsByAggreementAndLogUser(Aggreement aggreement, LogUser logUser, String sortFieldName, String sortOrder) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = ManagedDocument.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ManagedDocument AS o WHERE o.aggreement = :aggreement AND o.logUser = :logUser");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ManagedDocument> q = em.createQuery(queryBuilder.toString(), ManagedDocument.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("logUser", logUser);
        return q;
    }
    
    public static TypedQuery<ManagedDocument> ManagedDocument.findManagedDocumentsByIdAndLogUser(Long id, LogUser logUser) {
        if (id == null) throw new IllegalArgumentException("The id argument is required");
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = ManagedDocument.entityManager();
        TypedQuery<ManagedDocument> q = em.createQuery("SELECT o FROM ManagedDocument AS o WHERE o.id = :id AND o.logUser = :logUser", ManagedDocument.class);
        q.setParameter("id", id);
        q.setParameter("logUser", logUser);
        return q;
    }
    
    public static TypedQuery<ManagedDocument> ManagedDocument.findManagedDocumentsByIdAndLogUser(Long id, LogUser logUser, String sortFieldName, String sortOrder) {
        if (id == null) throw new IllegalArgumentException("The id argument is required");
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = ManagedDocument.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ManagedDocument AS o WHERE o.id = :id AND o.logUser = :logUser");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ManagedDocument> q = em.createQuery(queryBuilder.toString(), ManagedDocument.class);
        q.setParameter("id", id);
        q.setParameter("logUser", logUser);
        return q;
    }
    
    public static TypedQuery<ManagedDocument> ManagedDocument.findManagedDocumentsByLogUser(LogUser logUser) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = ManagedDocument.entityManager();
        TypedQuery<ManagedDocument> q = em.createQuery("SELECT o FROM ManagedDocument AS o WHERE o.logUser = :logUser", ManagedDocument.class);
        q.setParameter("logUser", logUser);
        return q;
    }
    
    public static TypedQuery<ManagedDocument> ManagedDocument.findManagedDocumentsByLogUser(LogUser logUser, String sortFieldName, String sortOrder) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = ManagedDocument.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ManagedDocument AS o WHERE o.logUser = :logUser");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ManagedDocument> q = em.createQuery(queryBuilder.toString(), ManagedDocument.class);
        q.setParameter("logUser", logUser);
        return q;
    }
    
    public static TypedQuery<ManagedDocument> ManagedDocument.findManagedDocumentsByLogUserAndParent(LogUser logUser, ManagedDocument parent) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = ManagedDocument.entityManager();
        TypedQuery<ManagedDocument> q = em.createQuery("SELECT o FROM ManagedDocument AS o WHERE o.logUser = :logUser AND o.parent = :parent", ManagedDocument.class);
        q.setParameter("logUser", logUser);
        q.setParameter("parent", parent);
        return q;
    }
    
    public static TypedQuery<ManagedDocument> ManagedDocument.findManagedDocumentsByLogUserAndParent(LogUser logUser, ManagedDocument parent, String sortFieldName, String sortOrder) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = ManagedDocument.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ManagedDocument AS o WHERE o.logUser = :logUser AND o.parent = :parent");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ManagedDocument> q = em.createQuery(queryBuilder.toString(), ManagedDocument.class);
        q.setParameter("logUser", logUser);
        q.setParameter("parent", parent);
        return q;
    }
    
    public static TypedQuery<ManagedDocument> ManagedDocument.findManagedDocumentsByLogUserAndParentIsNull(LogUser logUser) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = ManagedDocument.entityManager();
        TypedQuery<ManagedDocument> q = em.createQuery("SELECT o FROM ManagedDocument AS o WHERE o.logUser = :logUser AND o.parent IS NULL", ManagedDocument.class);
        q.setParameter("logUser", logUser);
        return q;
    }
    
    public static TypedQuery<ManagedDocument> ManagedDocument.findManagedDocumentsByLogUserAndParentIsNull(LogUser logUser, String sortFieldName, String sortOrder) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = ManagedDocument.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ManagedDocument AS o WHERE o.logUser = :logUser AND o.parent IS NULL");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ManagedDocument> q = em.createQuery(queryBuilder.toString(), ManagedDocument.class);
        q.setParameter("logUser", logUser);
        return q;
    }
    
    public static TypedQuery<ManagedDocument> ManagedDocument.findManagedDocumentsByLogUserAndType(LogUser logUser, ManagedDocumentType type) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        if (type == null) throw new IllegalArgumentException("The type argument is required");
        EntityManager em = ManagedDocument.entityManager();
        TypedQuery<ManagedDocument> q = em.createQuery("SELECT o FROM ManagedDocument AS o WHERE o.logUser = :logUser AND o.type = :type", ManagedDocument.class);
        q.setParameter("logUser", logUser);
        q.setParameter("type", type);
        return q;
    }
    
    public static TypedQuery<ManagedDocument> ManagedDocument.findManagedDocumentsByLogUserAndType(LogUser logUser, ManagedDocumentType type, String sortFieldName, String sortOrder) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        if (type == null) throw new IllegalArgumentException("The type argument is required");
        EntityManager em = ManagedDocument.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ManagedDocument AS o WHERE o.logUser = :logUser AND o.type = :type");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ManagedDocument> q = em.createQuery(queryBuilder.toString(), ManagedDocument.class);
        q.setParameter("logUser", logUser);
        q.setParameter("type", type);
        return q;
    }
    
}
