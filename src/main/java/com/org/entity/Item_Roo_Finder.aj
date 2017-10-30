// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.Item;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Item_Roo_Finder {
    
    public static Long Item.countFindItemsByAggreement(Aggreement aggreement) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Item AS o WHERE o.aggreement = :aggreement", Long.class);
        q.setParameter("aggreement", aggreement);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Item.countFindItemsByAggreementAndIsExtraItem(Aggreement aggreement, boolean isExtraItem) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Item AS o WHERE o.aggreement = :aggreement AND o.isExtraItem = :isExtraItem", Long.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("isExtraItem", isExtraItem);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Item.countFindItemsByAggreementAndIsExtraItemAndParentItemIsNull(Aggreement aggreement, boolean isExtraItem) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Item AS o WHERE o.aggreement = :aggreement AND o.isExtraItem = :isExtraItem AND o.parentItem IS NULL", Long.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("isExtraItem", isExtraItem);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Item.countFindItemsByAggreementAndLogUser(Aggreement aggreement, LogUser logUser) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Item AS o WHERE o.aggreement = :aggreement AND o.logUser = :logUser", Long.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("logUser", logUser);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Item.countFindItemsByAggreementAndMeasurementSheetId(Aggreement aggreement, Long measurementSheetId) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        if (measurementSheetId == null) throw new IllegalArgumentException("The measurementSheetId argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Item AS o WHERE o.aggreement = :aggreement AND o.measurementSheetId = :measurementSheetId", Long.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("measurementSheetId", measurementSheetId);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Item.countFindItemsByAggreementAndMeasurementSheetIdIsNullOrMeasurementSheetIdNotEquals(Aggreement aggreement, Long measurementSheetId) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        if (measurementSheetId == null) throw new IllegalArgumentException("The measurementSheetId argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Item AS o WHERE o.aggreement = :aggreement AND o.measurementSheetId IS NULL  OR o.measurementSheetId != :measurementSheetId", Long.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("measurementSheetId", measurementSheetId);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Item.countFindItemsByIdAndLogUser(Long id, LogUser logUser) {
        if (id == null) throw new IllegalArgumentException("The id argument is required");
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Item AS o WHERE o.id = :id AND o.logUser = :logUser", Long.class);
        q.setParameter("id", id);
        q.setParameter("logUser", logUser);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Item.countFindItemsByItemNumber(String itemNumber) {
        if (itemNumber == null || itemNumber.length() == 0) throw new IllegalArgumentException("The itemNumber argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Item AS o WHERE o.itemNumber = :itemNumber", Long.class);
        q.setParameter("itemNumber", itemNumber);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Item.countFindItemsByItemNumberAndAggreement(String itemNumber, Aggreement aggreement) {
        if (itemNumber == null || itemNumber.length() == 0) throw new IllegalArgumentException("The itemNumber argument is required");
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Item AS o WHERE o.itemNumber = :itemNumber AND o.aggreement = :aggreement", Long.class);
        q.setParameter("itemNumber", itemNumber);
        q.setParameter("aggreement", aggreement);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Item.countFindItemsByLogUser(LogUser logUser) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Item AS o WHERE o.logUser = :logUser", Long.class);
        q.setParameter("logUser", logUser);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<Item> Item.findItemsByAggreement(Aggreement aggreement) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery<Item> q = em.createQuery("SELECT o FROM Item AS o WHERE o.aggreement = :aggreement", Item.class);
        q.setParameter("aggreement", aggreement);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByAggreement(Aggreement aggreement, String sortFieldName, String sortOrder) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        EntityManager em = Item.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Item AS o WHERE o.aggreement = :aggreement");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Item> q = em.createQuery(queryBuilder.toString(), Item.class);
        q.setParameter("aggreement", aggreement);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByAggreementAndIsExtraItem(Aggreement aggreement, boolean isExtraItem) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery<Item> q = em.createQuery("SELECT o FROM Item AS o WHERE o.aggreement = :aggreement AND o.isExtraItem = :isExtraItem", Item.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("isExtraItem", isExtraItem);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByAggreementAndIsExtraItem(Aggreement aggreement, boolean isExtraItem, String sortFieldName, String sortOrder) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        EntityManager em = Item.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Item AS o WHERE o.aggreement = :aggreement AND o.isExtraItem = :isExtraItem");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Item> q = em.createQuery(queryBuilder.toString(), Item.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("isExtraItem", isExtraItem);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByAggreementAndIsExtraItemAndParentItemIsNull(Aggreement aggreement, boolean isExtraItem) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery<Item> q = em.createQuery("SELECT o FROM Item AS o WHERE o.aggreement = :aggreement AND o.isExtraItem = :isExtraItem AND o.parentItem IS NULL", Item.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("isExtraItem", isExtraItem);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByAggreementAndIsExtraItemAndParentItemIsNull(Aggreement aggreement, boolean isExtraItem, String sortFieldName, String sortOrder) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        EntityManager em = Item.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Item AS o WHERE o.aggreement = :aggreement AND o.isExtraItem = :isExtraItem AND o.parentItem IS NULL");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Item> q = em.createQuery(queryBuilder.toString(), Item.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("isExtraItem", isExtraItem);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByAggreementAndLogUser(Aggreement aggreement, LogUser logUser) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery<Item> q = em.createQuery("SELECT o FROM Item AS o WHERE o.aggreement = :aggreement AND o.logUser = :logUser", Item.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("logUser", logUser);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByAggreementAndLogUser(Aggreement aggreement, LogUser logUser, String sortFieldName, String sortOrder) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = Item.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Item AS o WHERE o.aggreement = :aggreement AND o.logUser = :logUser");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Item> q = em.createQuery(queryBuilder.toString(), Item.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("logUser", logUser);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByAggreementAndMeasurementSheetId(Aggreement aggreement, Long measurementSheetId) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        if (measurementSheetId == null) throw new IllegalArgumentException("The measurementSheetId argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery<Item> q = em.createQuery("SELECT o FROM Item AS o WHERE o.aggreement = :aggreement AND o.measurementSheetId = :measurementSheetId", Item.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("measurementSheetId", measurementSheetId);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByAggreementAndMeasurementSheetId(Aggreement aggreement, Long measurementSheetId, String sortFieldName, String sortOrder) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        if (measurementSheetId == null) throw new IllegalArgumentException("The measurementSheetId argument is required");
        EntityManager em = Item.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Item AS o WHERE o.aggreement = :aggreement AND o.measurementSheetId = :measurementSheetId");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Item> q = em.createQuery(queryBuilder.toString(), Item.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("measurementSheetId", measurementSheetId);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByAggreementAndMeasurementSheetIdIsNullOrMeasurementSheetIdNotEquals(Aggreement aggreement, Long measurementSheetId) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        if (measurementSheetId == null) throw new IllegalArgumentException("The measurementSheetId argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery<Item> q = em.createQuery("SELECT o FROM Item AS o WHERE o.aggreement = :aggreement AND o.measurementSheetId IS NULL  OR o.measurementSheetId != :measurementSheetId", Item.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("measurementSheetId", measurementSheetId);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByAggreementAndMeasurementSheetIdIsNullOrMeasurementSheetIdNotEquals(Aggreement aggreement, Long measurementSheetId, String sortFieldName, String sortOrder) {
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        if (measurementSheetId == null) throw new IllegalArgumentException("The measurementSheetId argument is required");
        EntityManager em = Item.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Item AS o WHERE o.aggreement = :aggreement AND o.measurementSheetId IS NULL  OR o.measurementSheetId != :measurementSheetId");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Item> q = em.createQuery(queryBuilder.toString(), Item.class);
        q.setParameter("aggreement", aggreement);
        q.setParameter("measurementSheetId", measurementSheetId);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByIdAndLogUser(Long id, LogUser logUser) {
        if (id == null) throw new IllegalArgumentException("The id argument is required");
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery<Item> q = em.createQuery("SELECT o FROM Item AS o WHERE o.id = :id AND o.logUser = :logUser", Item.class);
        q.setParameter("id", id);
        q.setParameter("logUser", logUser);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByIdAndLogUser(Long id, LogUser logUser, String sortFieldName, String sortOrder) {
        if (id == null) throw new IllegalArgumentException("The id argument is required");
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = Item.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Item AS o WHERE o.id = :id AND o.logUser = :logUser");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Item> q = em.createQuery(queryBuilder.toString(), Item.class);
        q.setParameter("id", id);
        q.setParameter("logUser", logUser);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByItemNumber(String itemNumber) {
        if (itemNumber == null || itemNumber.length() == 0) throw new IllegalArgumentException("The itemNumber argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery<Item> q = em.createQuery("SELECT o FROM Item AS o WHERE o.itemNumber = :itemNumber", Item.class);
        q.setParameter("itemNumber", itemNumber);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByItemNumber(String itemNumber, String sortFieldName, String sortOrder) {
        if (itemNumber == null || itemNumber.length() == 0) throw new IllegalArgumentException("The itemNumber argument is required");
        EntityManager em = Item.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Item AS o WHERE o.itemNumber = :itemNumber");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Item> q = em.createQuery(queryBuilder.toString(), Item.class);
        q.setParameter("itemNumber", itemNumber);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByItemNumberAndAggreement(String itemNumber, Aggreement aggreement) {
        if (itemNumber == null || itemNumber.length() == 0) throw new IllegalArgumentException("The itemNumber argument is required");
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery<Item> q = em.createQuery("SELECT o FROM Item AS o WHERE o.itemNumber = :itemNumber AND o.aggreement = :aggreement", Item.class);
        q.setParameter("itemNumber", itemNumber);
        q.setParameter("aggreement", aggreement);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByItemNumberAndAggreement(String itemNumber, Aggreement aggreement, String sortFieldName, String sortOrder) {
        if (itemNumber == null || itemNumber.length() == 0) throw new IllegalArgumentException("The itemNumber argument is required");
        if (aggreement == null) throw new IllegalArgumentException("The aggreement argument is required");
        EntityManager em = Item.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Item AS o WHERE o.itemNumber = :itemNumber AND o.aggreement = :aggreement");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Item> q = em.createQuery(queryBuilder.toString(), Item.class);
        q.setParameter("itemNumber", itemNumber);
        q.setParameter("aggreement", aggreement);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByLogUser(LogUser logUser) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = Item.entityManager();
        TypedQuery<Item> q = em.createQuery("SELECT o FROM Item AS o WHERE o.logUser = :logUser", Item.class);
        q.setParameter("logUser", logUser);
        return q;
    }
    
    public static TypedQuery<Item> Item.findItemsByLogUser(LogUser logUser, String sortFieldName, String sortOrder) {
        if (logUser == null) throw new IllegalArgumentException("The logUser argument is required");
        EntityManager em = Item.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Item AS o WHERE o.logUser = :logUser");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Item> q = em.createQuery(queryBuilder.toString(), Item.class);
        q.setParameter("logUser", logUser);
        return q;
    }
    
}
