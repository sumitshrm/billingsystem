package com.org.excel.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.org.constants.AbstractSheetConstants;

public class PartRateStatementRanges {
	
	
	private static final String strSlno = "PRS_SLNO";
	
	private static final String strDescriptionOfItem = "PRS_DESC_OF_ITEM";
	
	private static final String strFullRate = "PRS_FR";
	
	private static final String strPartRate = "PRS_PR";
	
	private static final String strRemarks = "PRS_REMARKS";
	
	private XLColumnRange rSlno;
	
	private XLColumnRange rDescriptionOfItem;
	
	private XLColumnRange rFullRate;
	
	private XLColumnRange rPartRate;
	
	private XLColumnRange rRemarks;
	
	
	public PartRateStatementRanges(XSSFWorkbook workbook) {
		super();
		this.rDescriptionOfItem = new XLColumnRange(workbook, strDescriptionOfItem);
		
		this.rSlno = new XLColumnRange(workbook, strSlno);
		this.rFullRate = new XLColumnRange(workbook, strFullRate);
		this.rPartRate = new XLColumnRange(workbook, strPartRate);
		this.rRemarks = new XLColumnRange(workbook, strRemarks);
		
	}


	public XLColumnRange getrSlno() {
		return rSlno;
	}


	public void setrSlno(XLColumnRange rSlno) {
		this.rSlno = rSlno;
	}


	public XLColumnRange getrDescriptionOfItem() {
		return rDescriptionOfItem;
	}


	public void setrDescriptionOfItem(XLColumnRange rDescriptionOfItem) {
		this.rDescriptionOfItem = rDescriptionOfItem;
	}


	public XLColumnRange getrFullRate() {
		return rFullRate;
	}


	public void setrFullRate(XLColumnRange rFullRate) {
		this.rFullRate = rFullRate;
	}


	public XLColumnRange getrPartRate() {
		return rPartRate;
	}


	public void setrPartRate(XLColumnRange rPartRate) {
		this.rPartRate = rPartRate;
	}


	public XLColumnRange getrRemarks() {
		return rRemarks;
	}


	public void setrRemarks(XLColumnRange rRemarks) {
		this.rRemarks = rRemarks;
	}
	
	

}
