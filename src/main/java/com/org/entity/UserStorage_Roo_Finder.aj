// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.domain.LogUser;
import com.org.entity.UserStorage;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect UserStorage_Roo_Finder {
    
    public static Long UserStorage.countFindUserStoragesByLogUser(LogUser logUser) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = UserStorage.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM UserStorage AS o WHERE o.logUser = :logUser", Long.class);
        q.setParameter("logUser", logUser);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<UserStorage> UserStorage.findUserStoragesByLogUser(LogUser logUser) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = UserStorage.entityManager();
        TypedQuery<UserStorage> q = em.createQuery("SELECT o FROM UserStorage AS o WHERE o.logUser = :logUser", UserStorage.class);
        q.setParameter("logUser", logUser);
        return q;
    }
    
    public static TypedQuery<UserStorage> UserStorage.findUserStoragesByLogUser(LogUser logUser, String sortFieldName, String sortOrder) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = UserStorage.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM UserStorage AS o WHERE o.logUser = :logUser");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<UserStorage> q = em.createQuery(queryBuilder.toString(), UserStorage.class);
        q.setParameter("logUser", logUser);
        return q;
    }
    
}
