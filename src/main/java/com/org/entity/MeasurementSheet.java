package com.org.entity;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.org.domain.LogUser;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.MeasurementComparatorBySerialNumber;
import com.org.util.ItemAbstractComparator;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findMeasurementSheetsByLogUser", "findMeasurementSheetsByIdAndLogUser" })
public class MeasurementSheet {

    @ManyToOne
    private Aggreement aggreement;

    @Size(max = 40)
    private String title;
    
    @NotNull 
    private Integer serialNumber;
    
    private boolean isFinalBill;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Document document;
    
    private boolean userManaged;
    
    private int templateVersion = 1;
    
    private transient String serialNumberDisplayFormat;
    
    @Transient
    private transient XSSFWorkbook workbook;
    
    @Transient
    private transient boolean itemAbstractSorted = false;
    
    @Transient
    private File excelFile;
    
    @Transient
    private boolean copyPreviousMeasurement;
    
    @Override
    public String toString() {
        return "MeasurementSheet:" + aggreement;
    }

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    @Column(updatable = false)
    private Date createDate;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date lastUpdatedDate;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date lastReportDate;

    @ManyToOne
    private LogUser logUser;
    
    @OneToMany(mappedBy = "measurementSheet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemAbstract> itemAbstracts = new ArrayList<ItemAbstract>();

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedDate = new Date();
    }

    @PrePersist
    protected void onCreate() {
        createDate = new Date();
        lastUpdatedDate = createDate;
        this.setLogUser(LogUser.getCurrentUser()); 
    }

    public String getDocumentFileName(){
    	String filename = this.getAggreement().getAggreementNum()+"_"+ title;
    	filename = filename.trim();
    	filename = filename.replaceAll("[-/ \\\\.]", "_");
    	return filename;
    }
    
    public String getStorageFileName() {
    	return "MSHEET_"+getId()+"_"+getAggreement().getId()+".xlsm";
    }
    
    public ItemAbstract getPreviousItemAbstract(Item item){
    	MeasurementSheet msheet = getPreviousMeasurementSheet();
    	if(msheet==null){
    		return null;
    	}
    	return ItemAbstract.getItemAbstractByItemAndMeasurementSheet(item, msheet, false);
    }
    
    public MeasurementSheet getPreviousMeasurementSheet(){
    	return this.getAggreement().getMeasurementSheetBySerialNumber(this.getSerialNumber()-1);
    }
    
    public ItemAbstract getItemAbstractByItemNum(String itemNum){
    	for(ItemAbstract abs :  this.getItemAbstracts()){
    		if(abs.getItem().getItemNumber().equals(itemNum)){
    			return abs;	
    		}
    	} 
    	return null;
    }
    

	public String getSerialNumberDisplayFormat() {
		int lastNumber = serialNumber % 10;
		if(serialNumber>3 && serialNumber<21)
			serialNumberDisplayFormat = serialNumber + "th";
		else if(lastNumber == 1)
			serialNumberDisplayFormat = serialNumber + "st";
		else if(lastNumber==2)
			serialNumberDisplayFormat = serialNumber + "nd";
		else if(lastNumber == 3)
			serialNumberDisplayFormat = serialNumber + "rd";
		if(isFinalBill)
			serialNumberDisplayFormat += " & Final Bill";
		else if(serialNumberDisplayFormat!=null && !serialNumberDisplayFormat.trim().equals(""))
			serialNumberDisplayFormat +=" R/A Bill";
		return serialNumberDisplayFormat;
	}

	public void setSerialNumberDisplayFormat(String serialNumberDisplayFormat) {
		this.serialNumberDisplayFormat = serialNumberDisplayFormat;
	}
	
	public boolean isFirstAndFinalBill(){
		if(serialNumber==1 && isIsFinalBill())
			return true;
		else
			return false;
	}

	public int getTemplateVersion() {
		return templateVersion;
	}

	public void setTemplateVersion(int templateVersion) {
		this.templateVersion = templateVersion;
	}

	public boolean isItemAbstractSorted() {
		return itemAbstractSorted;
	}

	public void setItemAbstractSorted(boolean itemAbstractSorted) {
		this.itemAbstractSorted = itemAbstractSorted;
	}

	public List<ItemAbstract> getItemAbstractsSorted(){
		List<ItemAbstract> itemAbstracts = getItemAbstracts();
		if(!isItemAbstractSorted()){
			Collections.sort(itemAbstracts, new ItemAbstractComparator());
			setItemAbstractSorted(true);
		}
		return itemAbstracts;
	}
	
	public Map<String, ItemAbstract> getItemAbstractsMap(){
		Map<String, ItemAbstract> itemAbstractMap = new LinkedHashMap<String, ItemAbstract>();
		for(ItemAbstract itemAbstract : getItemAbstracts()){
			itemAbstractMap.put(itemAbstract.getItem().getItemNumber(), itemAbstract);
		}
		return itemAbstractMap;
	}
	
	public Map<String, ItemAbstract> getItemAbstractsMapForDeviation(){
		Map<String, ItemAbstract> itemAbstractMap = new LinkedHashMap<String, ItemAbstract>();
		for(ItemAbstract itemAbstract : getItemAbstracts()){
			if(itemAbstract.getTotalDeviation()!=0){
				itemAbstractMap.put(itemAbstract.getItem().getItemNumber(), itemAbstract);
			}
		}
		return itemAbstractMap;
	}

	public boolean isCopyPreviousMeasurement() {
		return copyPreviousMeasurement;
	}

	public void setCopyPreviousMeasurement(boolean copyPreviousMeasurement) {
		this.copyPreviousMeasurement = copyPreviousMeasurement;
	}

}
