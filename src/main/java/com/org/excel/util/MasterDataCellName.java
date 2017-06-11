package com.org.excel.util;

import com.org.constants.Worksheets;


public class MasterDataCellName {

	public static String NAME_OF_WORK = "NAME_OF_WORK";
	public static String AGENCY = "AGENCY";
	public static String AGGREEMENT_NO = "AGGREEMENT_NUM";
	public static String DATE_OF_START = "DOS";
	public static String DATE_OF_COMPLETION_S = "DOC_S";
	public static String DATE_OF_COMPLETION_A = "DOC_A";
	public static String SNO_OF_BILL = "SNO_OF_BILL";
	public static String CLAUSE = "CLAUSE";
	public static String DATE_OF_ABSTRACT = "DOA";
	public static String TENDER_COST = "TENDER_COST";
	public static String ESTIMATED_COST = "ESTIMATED_COST";
	public static String DIVISION = "DIVISION";
	public static String SUB_DIVISION = "SUB_DIVISION";
	
	public static String T_MEASUREMENT_SHEET_ID = "MEASUREMENT_SHEET_ID";
	
	public static String getCellNameBySheet(String cellName ,String sheetName){
		return Worksheets.getShortName(sheetName)+"_"+cellName;
	}
	
}
