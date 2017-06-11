package com.org.excel.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DeviationRanges {
	
	public static final String STR_SL_NO = "DEV_SL_NO";

	private static final String STR_ITEMNUM = "DEV_ITEM_NUM";
	
	private static final String STR_DESCRIPTIONOFITEM = "DEV_DESCRIPTION";
	
	private static final String STR_UNIT = "DEV_UNIT";
	
	private static final String STR_QTY_AP_AGMT = "DEV_QTY_AP_AGMT";
	
	private static final String STR_QTY_EUD = "DEV_QTY_EUD";
	
	private static final String STR_DEV_DEV_QTY = "DEV_DEV_QTY";
	
	private static final String STR_RATE = "DEV_RATE";
	
	public static final String STR_LESS_CA = "DEV_LESS_CA";
	
	private static final String STR_RATE_PROPOSED = "DEV_RATE_PROPOSED";
	
	private static final String STR_DEV_PERCENT = "DEV_DEV_PERCENT";
	
	private static final String STR_DEV_PLUS="DEV_DEV_PLUS";
	
	private static final String STR_DEV_MINUS = "DEV_DEV_MINUS";
	
	private static final String STR_TOTAL_DEV = "DEV_TOTAL_DEV";
	
	private static final String STR_SUMARY_DEV_PLUS="DEV_SUMARY_DEV_PLUS";
	
	private static final String STR_SUMARY_DEV_MINUS = "DEV_SUMARY_DEV_MINUS";
	
	private static final String STR_SUMARY_TOTAL_DEV = "DEV_SUMARY_TOTAL_DEV";
	
	private static final String STR_SUMARY_DEV_PERCENT="DEV_SUMARY_DEV_PERCENT";
	
	private XLColumnRange rSlno ;
	
	private XLColumnRange rItemNum ;
	
	private XLColumnRange rDescription ;
	
	private XLColumnRange rUnit;
	
	private XLColumnRange rQtyApAgmt ;
	
	private XLColumnRange rQtyEud;
	
	private XLColumnRange rDevQty;
	
	private XLColumnRange rRate;
	
	private XLColumnRange rLessCa;
	
	private XLColumnRange rRateProposed;
	
	private XLColumnRange rDevPercent;
	
	private XLColumnRange rDevPlus;
	
	private XLColumnRange rDevMinus ;
	
	private XLColumnRange rTotalDev ;
	
	private XLColumnRange rSumaryDevPlus;
	
	private XLColumnRange rSumaryDevMinus;
	
	private XLColumnRange rSumaryTotalDev;
	
	private XLColumnRange rSumaryDevPercent;

	public DeviationRanges(XSSFWorkbook workbook) {
		super();
		//= new XLColumnRange(workbook, 
		this.rSlno = new XLColumnRange(workbook,STR_SL_NO);
		this.rItemNum = new XLColumnRange(workbook,STR_ITEMNUM);
		this.rDescription = new XLColumnRange(workbook,STR_DESCRIPTIONOFITEM);
		this.rUnit = new XLColumnRange(workbook,STR_UNIT);
		this.rQtyApAgmt = new XLColumnRange(workbook,STR_QTY_AP_AGMT);
		this.rQtyEud = new XLColumnRange(workbook,STR_QTY_EUD);
		this.rDevQty = new XLColumnRange(workbook,STR_DEV_DEV_QTY);
		this.rRate = new XLColumnRange(workbook,STR_RATE);
		this.rLessCa = new XLColumnRange(workbook,STR_LESS_CA);
		this.rRateProposed = new XLColumnRange(workbook,STR_RATE_PROPOSED);
		this.rDevPercent = new XLColumnRange(workbook,STR_DEV_PERCENT);
		this.rDevPlus = new XLColumnRange(workbook,STR_DEV_PLUS);
		this.rDevMinus = new XLColumnRange(workbook,STR_DEV_MINUS);
		this.rTotalDev = new XLColumnRange(workbook,STR_TOTAL_DEV);
		this.rSumaryDevPlus = new XLColumnRange(workbook,STR_SUMARY_DEV_PLUS);
		this.rSumaryDevMinus = new XLColumnRange(workbook,STR_SUMARY_DEV_MINUS);
		this.rSumaryTotalDev = new XLColumnRange(workbook,STR_SUMARY_TOTAL_DEV);
		this.rSumaryDevPercent = new XLColumnRange(workbook,STR_SUMARY_DEV_PERCENT);
	}

	public XLColumnRange getrSlno() {
		return rSlno;
	}

	public XLColumnRange getrItemNum() {
		return rItemNum;
	}

	public XLColumnRange getrDescription() {
		return rDescription;
	}

	public XLColumnRange getrUnit() {
		return rUnit;
	}

	public XLColumnRange getrQtyApAgmt() {
		return rQtyApAgmt;
	}

	public XLColumnRange getrQtyEud() {
		return rQtyEud;
	}

	public XLColumnRange getrDevQty() {
		return rDevQty;
	}

	public XLColumnRange getrRate() {
		return rRate;
	}

	public XLColumnRange getrLessCa() {
		return rLessCa;
	}

	public XLColumnRange getrRateProposed() {
		return rRateProposed;
	}

	public XLColumnRange getrDevPercent() {
		return rDevPercent;
	}

	public XLColumnRange getrDevPlus() {
		return rDevPlus;
	}

	public XLColumnRange getrDevMinus() {
		return rDevMinus;
	}

	public XLColumnRange getrTotalDev() {
		return rTotalDev;
	}

	public XLColumnRange getrSumaryDevPlus() {
		return rSumaryDevPlus;
	}

	public XLColumnRange getrSumaryDevMinus() {
		return rSumaryDevMinus;
	}

	public XLColumnRange getrSumaryTotalDev() {
		return rSumaryTotalDev;
	}

	public XLColumnRange getrSumaryDevPercent() {
		return rSumaryDevPercent;
	}

	
	
}
