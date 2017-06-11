package com.org.excel.gateway;

import java.util.ArrayList;
import java.util.List;

import com.org.entity.Item;

public class ExcelGatewayTo {

	private String itemsAsJSON;
	
	private List<String> itemNumbers = new ArrayList<String>();
	
	private ResponseStatus status;
	
	private String message;
	
	private Long aggreementId;
	
	private Long measurementSheetId;
	
	
	public String getItemsAsJSON() {
		return itemsAsJSON;
	}
	public void setItemsAsJSON(String itemsAsJSON) {
		this.itemsAsJSON = itemsAsJSON;
	}
	public List<String> getItemNumbers() {
		return itemNumbers;
	}
	public void setItemNumbers(List<String> itemNumbers) {
		this.itemNumbers = itemNumbers;
	}
	public ResponseStatus getStatus() {
		return status;
	}
	public void setStatus(ResponseStatus status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getAggreementId() {
		return aggreementId;
	}
	public void setAggreementId(Long aggreementId) {
		this.aggreementId = aggreementId;
	}
	public Long getMeasurementSheetId() {
		return measurementSheetId;
	}
	public void setMeasurementSheetId(Long measurementSheetId) {
		this.measurementSheetId = measurementSheetId;
	}
	
	
	
}
