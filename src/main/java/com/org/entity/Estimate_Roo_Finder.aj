// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.domain.LogUser;
import com.org.entity.Estimate;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Estimate_Roo_Finder {
    
    public static Long Estimate.countFindEstimatesByLogUser(LogUser logUser) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = Estimate.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Estimate AS o WHERE o.logUser = :logUser", Long.class);
        q.setParameter("logUser", logUser);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<Estimate> Estimate.findEstimatesByLogUser(LogUser logUser) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = Estimate.entityManager();
        TypedQuery<Estimate> q = em.createQuery("SELECT o FROM Estimate AS o WHERE o.logUser = :logUser", Estimate.class);
        q.setParameter("logUser", logUser);
        return q;
    }
    
    public static TypedQuery<Estimate> Estimate.findEstimatesByLogUser(LogUser logUser, String sortFieldName, String sortOrder) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = Estimate.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Estimate AS o WHERE o.logUser = :logUser");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Estimate> q = em.createQuery(queryBuilder.toString(), Estimate.class);
        q.setParameter("logUser", logUser);
        return q;
    }
    
}
