// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.entity.EstimateShared;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect EstimateShared_Roo_Jpa_Entity {
    
    declare @type: EstimateShared: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long EstimateShared.id;
    
    @Version
    @Column(name = "version")
    private Integer EstimateShared.version;
    
    public Long EstimateShared.getId() {
        return this.id;
    }
    
    public void EstimateShared.setId(Long id) {
        this.id = id;
    }
    
    public Integer EstimateShared.getVersion() {
        return this.version;
    }
    
    public void EstimateShared.setVersion(Integer version) {
        this.version = version;
    }
    
}