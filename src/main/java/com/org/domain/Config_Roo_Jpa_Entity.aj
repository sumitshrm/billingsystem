// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.domain;

import com.org.domain.Config;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect Config_Roo_Jpa_Entity {
    
    declare @type: Config: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Config.id;
    
    @Version
    @Column(name = "version")
    private Integer Config.version;
    
    public Long Config.getId() {
        return this.id;
    }
    
    public void Config.setId(Long id) {
        this.id = id;
    }
    
    public Integer Config.getVersion() {
        return this.version;
    }
    
    public void Config.setVersion(Integer version) {
        this.version = version;
    }
    
}
