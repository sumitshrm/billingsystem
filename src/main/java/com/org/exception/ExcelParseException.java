package com.org.exception;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellReference;

public class ExcelParseException extends Exception{
	
	String itemNum;
	
	String rowNum;
	
	String colNum;
	
	public ExcelParseException(String msg) {
		super(msg);
	}
	
	public ExcelParseException(String sheetName, String itemNum, String rowNum, String colNum, Throwable cause) {
		super("Exception while parsing excel, sheet: "+sheetName+" itemNum : "+itemNum+", rowNum: "+rowNum+", colNum: "+colNum);
		this.itemNum = itemNum;
		this.rowNum = rowNum;
		this.colNum = colNum;
	}
	
	public ExcelParseException(Cell cell) {
		super("Exception occured at : " + (new CellReference(cell)).formatAsString());
	}
	
}
