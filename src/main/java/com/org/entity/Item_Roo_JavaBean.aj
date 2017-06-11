// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.org.entity;

import com.org.domain.LogUser;
import com.org.entity.Aggreement;
import com.org.entity.Item;
import com.org.entity.ItemAbstract;
import java.util.List;

privileged aspect Item_Roo_JavaBean {
    
    public String Item.getItemNumber() {
        return this.itemNumber;
    }
    
    public void Item.setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }
    
    public void Item.setSubItemNumber(String subItemNumber) {
        this.subItemNumber = subItemNumber;
    }
    
    public Aggreement Item.getAggreement() {
        return this.aggreement;
    }
    
    public void Item.setAggreement(Aggreement aggreement) {
        this.aggreement = aggreement;
    }
    
    public Item Item.getParentItem() {
        return this.parentItem;
    }
    
    public void Item.setParentItem(Item parentItem) {
        this.parentItem = parentItem;
    }
    
    public List<Item> Item.getSubItems() {
        return this.subItems;
    }
    
    public void Item.setSubItems(List<Item> subItems) {
        this.subItems = subItems;
    }
    
    public Long Item.getSortOrder() {
        return this.sortOrder;
    }
    
    public void Item.setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public String Item.getDescription() {
        return this.description;
    }
    
    public void Item.setDescription(String description) {
        this.description = description;
    }
    
    public String Item.getUnit() {
        return this.unit;
    }
    
    public void Item.setUnit(String unit) {
        this.unit = unit;
    }
    
    public Double Item.getQuantityPerUnit() {
        return this.quantityPerUnit;
    }
    
    public void Item.setQuantityPerUnit(Double quantityPerUnit) {
        this.quantityPerUnit = quantityPerUnit;
    }
    
    public boolean Item.isIsExtraItem() {
        return this.isExtraItem;
    }
    
    public void Item.setIsExtraItem(boolean isExtraItem) {
        this.isExtraItem = isExtraItem;
    }
    
    public Double Item.getFullRate() {
        return this.fullRate;
    }
    
    public void Item.setFullRate(Double fullRate) {
        this.fullRate = fullRate;
    }
    
    public Double Item.getPartRate() {
        return this.partRate;
    }
    
    public void Item.setPartRate(Double partRate) {
        this.partRate = partRate;
    }
    
    public Double Item.getQuantity() {
        return this.quantity;
    }
    
    public void Item.setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    
    public String Item.getDrsCode() {
        return this.drsCode;
    }
    
    public void Item.setDrsCode(String drsCode) {
        this.drsCode = drsCode;
    }
    
    public Double Item.getDsrRate() {
        return this.dsrRate;
    }
    
    public void Item.setDsrRate(Double dsrRate) {
        this.dsrRate = dsrRate;
    }
    
    public Long Item.getMeasurementSheetId() {
        return this.measurementSheetId;
    }
    
    public void Item.setMeasurementSheetId(Long measurementSheetId) {
        this.measurementSheetId = measurementSheetId;
    }
    
    public List<ItemAbstract> Item.getItemAbstracts() {
        return this.itemAbstracts;
    }
    
    public void Item.setItemAbstracts(List<ItemAbstract> itemAbstracts) {
        this.itemAbstracts = itemAbstracts;
    }
    
    public LogUser Item.getLogUser() {
        return this.logUser;
    }
    
    public void Item.setLogUser(LogUser logUser) {
        this.logUser = logUser;
    }
    
}
