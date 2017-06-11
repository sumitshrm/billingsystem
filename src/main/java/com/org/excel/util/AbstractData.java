package com.org.excel.util;

import com.org.entity.Item;

public class AbstractData {
	Item item;
	

	String itemNumber;

	String description;

	String quantityFormula;
	
	Double quantityPerUnit;

	String unit;

	Double fullRate;

	Double partRate;

	String amountFormula;

	public AbstractData() {
		super();
	}

	public AbstractData(String itemNumber, String description) {
		super();
		this.itemNumber = itemNumber;
		this.description = description;
	}

	public AbstractData(String itemNumber, String quantityFormula, String unit, Double fullRate,
			Double partRate, String amountFormula, Double quantityPerUnit) {
		super();
		this.itemNumber = itemNumber;
		this.quantityFormula = quantityFormula;
		this.unit = unit;
		this.fullRate = fullRate;
		this.partRate = partRate;
		this.amountFormula = amountFormula;
		this.quantityPerUnit = quantityPerUnit;
	}

	public AbstractData(String itemNumber,String description, String quantityFormula, String unit) {
		super();
		this.itemNumber = itemNumber;
		this.description = description;
		this.quantityFormula = quantityFormula;
		this.unit = unit;
	}

	public AbstractData(String itemNumber, String description,
			String quantityFormula, String unit, Double fullRate,
			Double partRate, String amountFormula) {
		super();
		this.itemNumber = itemNumber;
		this.description = description;
		this.quantityFormula = quantityFormula;
		this.unit = unit;
		this.fullRate = fullRate;
		this.partRate = partRate;
		this.amountFormula = amountFormula;
	}

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQuantityFormula() {
		return quantityFormula;
	}

	public void setQuantityFormula(String quantityFormula) {
		this.quantityFormula = quantityFormula;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Double getFullRate() {
		return fullRate;
	}

	public void setFullRate(Double fullRate) {
		this.fullRate = fullRate;
	}

	public Double getPartRate() {
		return partRate;
	}

	public void setPartRate(Double partRate) {
		this.partRate = partRate;
	}

	public String getAmountFormula() {
		return amountFormula;
	}

	public void setAmountFormula(String amountFormula) {
		this.amountFormula = amountFormula;
	}

	public Double getQuantityPerUnit() {
		return quantityPerUnit;
	}

	public void setQuantityPerUnit(Double quantityPerUnit) {
		this.quantityPerUnit = quantityPerUnit;
	}
	
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}


}
