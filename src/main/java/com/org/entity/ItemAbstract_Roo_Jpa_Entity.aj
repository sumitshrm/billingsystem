// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.entity.ItemAbstract;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect ItemAbstract_Roo_Jpa_Entity {
    
    declare @type: ItemAbstract: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long ItemAbstract.id;
    
    @Version
    @Column(name = "version")
    private Integer ItemAbstract.version;
    
    public Long ItemAbstract.getId() {
        return this.id;
    }
    
    public void ItemAbstract.setId(Long id) {
        this.id = id;
    }
    
    public Integer ItemAbstract.getVersion() {
        return this.version;
    }
    
    public void ItemAbstract.setVersion(Integer version) {
        this.version = version;
    }
    
}
