package com.org.excel.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExtraItemStatementRanges {
	
	public static final String STR_SL_NO = "EIS_SL_NO";

	private static final String STR_ITEMNUM = "EIS_ITEM_NUM";
	
	private static final String STR_DSR_CODE = "EIS_DSR_CODE";
	
	private static final String STR_DESCRIPTIONOFITEM = "EIS_DESCRIPTION";
	
	private static final String STR_UNIT = "EIS_UNIT";
	
	private static final String STR_QTY_EUD = "EIS_QTY_EUD";
	
	private static final String STR_RATE = "EIS_RATE";
	
	public static final String STR_LESS_CA = "EIS_LESS_CA";
	
	private static final String STR_RATE_PROPOSED = "EIS_RATE_PROPOSED";
	
	private static final String STR_TOTAL_AMOUNT = "EIS_TOTAL_AMOUNT";
	
	private static final String STR_REMARKS="EIS_REMARKS";
	
	private XLColumnRange rSlno ;
	
	private XLColumnRange rItemNum ;
	
	private XLColumnRange rDsrCode ;
	
	private XLColumnRange rDescription ;
	
	private XLColumnRange rUnit;
	
	private XLColumnRange rQtyEud;
	
	private XLColumnRange rRate;
	
	private XLColumnRange rLessCa;
	
	private XLColumnRange rRateProposed;
	
	private XLColumnRange rTotalAmount ;
	
	private XLColumnRange rRemarks;
	
	public ExtraItemStatementRanges(XSSFWorkbook workbook) {
		super();
		//= new XLColumnRange(workbook, 
		this.rSlno = new XLColumnRange(workbook,STR_SL_NO);
		this.rItemNum = new XLColumnRange(workbook,STR_ITEMNUM);
		this.rDsrCode = new XLColumnRange(workbook, STR_DSR_CODE);
		this.rDescription = new XLColumnRange(workbook,STR_DESCRIPTIONOFITEM);
		this.rUnit = new XLColumnRange(workbook,STR_UNIT);
		this.rQtyEud = new XLColumnRange(workbook,STR_QTY_EUD);
		this.rRate = new XLColumnRange(workbook,STR_RATE);
		this.rLessCa = new XLColumnRange(workbook,STR_LESS_CA);
		this.rRateProposed = new XLColumnRange(workbook,STR_RATE_PROPOSED);
		this.rTotalAmount = new XLColumnRange(workbook, STR_TOTAL_AMOUNT);
		this.rRemarks = new XLColumnRange(workbook, STR_REMARKS);
	}

	public XLColumnRange getrSlno() {
		return rSlno;
	}

	public void setrSlno(XLColumnRange rSlno) {
		this.rSlno = rSlno;
	}

	public XLColumnRange getrItemNum() {
		return rItemNum;
	}

	public void setrItemNum(XLColumnRange rItemNum) {
		this.rItemNum = rItemNum;
	}

	public XLColumnRange getrDsrCode() {
		return rDsrCode;
	}

	public void setrDsrCode(XLColumnRange rDsrCode) {
		this.rDsrCode = rDsrCode;
	}

	public XLColumnRange getrDescription() {
		return rDescription;
	}

	public void setrDescription(XLColumnRange rDescription) {
		this.rDescription = rDescription;
	}

	public XLColumnRange getrUnit() {
		return rUnit;
	}

	public void setrUnit(XLColumnRange rUnit) {
		this.rUnit = rUnit;
	}

	public XLColumnRange getrQtyEud() {
		return rQtyEud;
	}

	public void setrQtyEud(XLColumnRange rQtyEud) {
		this.rQtyEud = rQtyEud;
	}

	public XLColumnRange getrRate() {
		return rRate;
	}

	public void setrRate(XLColumnRange rRate) {
		this.rRate = rRate;
	}

	public XLColumnRange getrLessCa() {
		return rLessCa;
	}

	public void setrLessCa(XLColumnRange rLessCa) {
		this.rLessCa = rLessCa;
	}

	public XLColumnRange getrRateProposed() {
		return rRateProposed;
	}

	public void setrRateProposed(XLColumnRange rRateProposed) {
		this.rRateProposed = rRateProposed;
	}

	public XLColumnRange getrTotalAmount() {
		return rTotalAmount;
	}

	public void setrTotalAmount(XLColumnRange rTotalAmount) {
		this.rTotalAmount = rTotalAmount;
	}

	public XLColumnRange getrRemarks() {
		return rRemarks;
	}

	public void setrRemarks(XLColumnRange rRemarks) {
		this.rRemarks = rRemarks;
	}

	

	
	
}
