// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.domain.LogUser;
import com.org.entity.UserStorage;

privileged aspect UserStorage_Roo_JavaBean {
    
    public long UserStorage.getStorageLimit() {
        return this.storageLimit;
    }
    
    public void UserStorage.setStorageLimit(long storageLimit) {
        this.storageLimit = storageLimit;
    }
    
    public LogUser UserStorage.getLogUser() {
        return this.logUser;
    }
    
    public void UserStorage.setLogUser(LogUser logUser) {
        this.logUser = logUser;
    }
    
}