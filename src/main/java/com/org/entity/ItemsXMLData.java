package com.org.entity;
import javax.xml.bind.annotation.XmlElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemsXMLData {
	private String itemNumber;
	
	private String unit;
	
    private String description;
	
    private Double fullRate;
    
    public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setFullRate(Double fullRate) {
		this.fullRate = fullRate;
	}
	@XmlElement(name="code")
	public String getItemNumber() {
		return itemNumber;
	}
	@XmlElement(name="unit")
	public String getUnit() {
		return unit;
	}
	@XmlElement(name="desc")
	public String getDescription() {
		return description;
	}
	@XmlElement(name="rate")
	public Double getFullRate() {
		return fullRate;
	}
}
