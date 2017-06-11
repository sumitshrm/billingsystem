package com.org.entity;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

import com.org.util.StrUtil;

public class ItemAbstractDataTo {
	
	private double total;
    
    private String measCellRef;
    
    private String absCellRef;
    
    private XSSFRow configDataRow;
    

	public XSSFRow getConfigDataRow() {
		return configDataRow;
	}

	public void setConfigDataRow(XSSFRow configDataRow) {
		this.configDataRow = configDataRow;
	}

	public String getAbsCellRef() {
		return absCellRef;
	}

	public void setAbsCellRef(String absCellRef) {
		this.absCellRef = absCellRef;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getMeasCellRef() {
		return measCellRef;
	}
	
	public String getMeasCellRefHyperlink(){
		int rowNum = StrUtil.extractNumbers(measCellRef);
		return "A" + rowNum;
	}

	public void setMeasCellRef(String measCellRef) {
		this.measCellRef = measCellRef;
	}

}
