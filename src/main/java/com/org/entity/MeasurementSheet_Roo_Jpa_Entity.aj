// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.entity.MeasurementSheet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

privileged aspect MeasurementSheet_Roo_Jpa_Entity {
    
    declare @type: MeasurementSheet: @Entity;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long MeasurementSheet.id;
    
    @Version
    @Column(name = "version")
    private Integer MeasurementSheet.version;
    
    public Long MeasurementSheet.getId() {
        return this.id;
    }
    
    public void MeasurementSheet.setId(Long id) {
        this.id = id;
    }
    
    public Integer MeasurementSheet.getVersion() {
        return this.version;
    }
    
    public void MeasurementSheet.setVersion(Integer version) {
        this.version = version;
    }
    
}
