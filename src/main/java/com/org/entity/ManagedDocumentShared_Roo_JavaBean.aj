// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.domain.LogUser;
import com.org.entity.ManagedDocument;
import com.org.entity.ManagedDocumentShared;
import java.util.Date;

privileged aspect ManagedDocumentShared_Roo_JavaBean {
    
    public Date ManagedDocumentShared.getSharedDate() {
        return this.sharedDate;
    }
    
    public void ManagedDocumentShared.setSharedDate(Date sharedDate) {
        this.sharedDate = sharedDate;
    }
    
    public LogUser ManagedDocumentShared.getSharedWith() {
        return this.sharedWith;
    }
    
    public void ManagedDocumentShared.setSharedWith(LogUser sharedWith) {
        this.sharedWith = sharedWith;
    }
    
    public ManagedDocument ManagedDocumentShared.getManagedDocument() {
        return this.managedDocument;
    }
    
    public void ManagedDocumentShared.setManagedDocument(ManagedDocument managedDocument) {
        this.managedDocument = managedDocument;
    }
    
    public boolean ManagedDocumentShared.isOpened() {
        return this.opened;
    }
    
    public void ManagedDocumentShared.setOpened(boolean opened) {
        this.opened = opened;
    }
    
}
