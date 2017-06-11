package com.org.excel.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class RunningBillScheduleRange extends ScheduleRange {
	
	private static String RB_PREFIX = "RB_SCHEDULE";
	
	private static final String strFullRate = "FULL_RATE";
	
	private static final String strPartRate = "PART_RATE";
	
	private static final String strUpToDate = "UTD";
	
	private static final String strUptoPreviousBill = "UPB";
	
	private static final String strSincePreviousBill = "SPB";
	
	private XLColumnRange rFullRate;
	
	private XLColumnRange rPartRate;
	
	private XLColumnRange rUpToDate;
	
	private XLColumnRange rUptoPreviousBill;
	
	private XLColumnRange rSincePreviousBill;
	
	public RunningBillScheduleRange(XSSFWorkbook workbook) {
		super(workbook);
		this.rFullRate = new XLColumnRange(workbook, withPrefix(strFullRate));
		this.rPartRate = new XLColumnRange(workbook, withPrefix(strPartRate));
		this.rUpToDate = new XLColumnRange(workbook, withPrefix(strUpToDate));
		this.rUptoPreviousBill = new XLColumnRange(workbook, withPrefix(strUptoPreviousBill));
		this.rSincePreviousBill = new XLColumnRange(workbook, withPrefix(strSincePreviousBill));
	}

	public static String getStrfullrate() {
		return strFullRate;
	}

	public static String getStrpartrate() {
		return strPartRate;
	}

	public static String getStruptodate() {
		return strUpToDate;
	}

	public static String getStruptopreviousbill() {
		return strUptoPreviousBill;
	}

	public static String getStrsincepreviousbill() {
		return strSincePreviousBill;
	}

	public XLColumnRange getrFullRate() {
		return rFullRate;
	}

	public XLColumnRange getrPartRate() {
		return rPartRate;
	}

	public XLColumnRange getrUpToDate() {
		return rUpToDate;
	}
	
	@Override
	public String getPrefix(){
		return RB_PREFIX;
	}

	public XLColumnRange getrUptoPreviousBill() {
		return rUptoPreviousBill;
	}

	public XLColumnRange getrSincePreviousBill() {
		return rSincePreviousBill;
	}


}
