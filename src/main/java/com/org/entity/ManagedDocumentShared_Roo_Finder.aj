// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.entity.ManagedDocument;
import com.org.entity.ManagedDocumentShared;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect ManagedDocumentShared_Roo_Finder {
    
    public static Long ManagedDocumentShared.countFindManagedDocumentSharedsByManagedDocument(ManagedDocument managedDocument) {
        if (managedDocument == null) throw new IllegalArgumentException("The managedDocument argument is required");
        EntityManager em = ManagedDocumentShared.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ManagedDocumentShared AS o WHERE o.managedDocument = :managedDocument", Long.class);
        q.setParameter("managedDocument", managedDocument);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<ManagedDocumentShared> ManagedDocumentShared.findManagedDocumentSharedsByManagedDocument(ManagedDocument managedDocument) {
        if (managedDocument == null) throw new IllegalArgumentException("The managedDocument argument is required");
        EntityManager em = ManagedDocumentShared.entityManager();
        TypedQuery<ManagedDocumentShared> q = em.createQuery("SELECT o FROM ManagedDocumentShared AS o WHERE o.managedDocument = :managedDocument", ManagedDocumentShared.class);
        q.setParameter("managedDocument", managedDocument);
        return q;
    }
    
    public static TypedQuery<ManagedDocumentShared> ManagedDocumentShared.findManagedDocumentSharedsByManagedDocument(ManagedDocument managedDocument, String sortFieldName, String sortOrder) {
        if (managedDocument == null) throw new IllegalArgumentException("The managedDocument argument is required");
        EntityManager em = ManagedDocumentShared.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ManagedDocumentShared AS o WHERE o.managedDocument = :managedDocument");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ManagedDocumentShared> q = em.createQuery(queryBuilder.toString(), ManagedDocumentShared.class);
        q.setParameter("managedDocument", managedDocument);
        return q;
    }
    
}
