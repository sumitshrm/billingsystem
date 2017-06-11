package com.org.constants;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public class Worksheets {

	public static final String MEASUREMENTSHEET = "MEASUREMENT";
	
	public static final String MEASUREMENTSHEET_SHORT = "M";
	public static final String ITEMS_SHEET = "ITEMS";
	public static final String EXTRA_ITEMS_SHEET = "EXTRA_ITEMS";
	public static final String TEMPSHEET = "TEMPLATE";
	public static final String TEMPSHEET_SHORT = "T";
	public static final String ABSTRACTSHEET = "ABSTRACT";
	public static final String ABSTRACTSHEET_SHORT = "A";
	public static final String RB_SCHEDULE = "RB_SCHEDULE";
	public static final String RB_SCHEDULE_SHORT = "RB_SCHEDULE";
	public static final String FNFB_SCHEDULE = "FNFB_SCHEDULE";
	public static final String FNFB_SCHEDULE_SHORT = "FNFB_SCHEDULE";
	public static final String DEVIATIONSHEET = "DEVIATION";
	public static final String DEVIATION_SHORT = "DEV";
	public static final String BILLFORMS_MAIN_SHEET  = "MAIN";
	public static final String BILLFORMS_MAIN_SHEET_SHORT  = "BF";
	public static final String CONFIG_DATA_SHEET = "CONFIG_DATA";
	public static final String PART_RATE_STATEMENT_SHEET = "PART_RATE_STATEMENT";
	public static final String PART_RATE_STATEMENT_SHEET_SHORT = "PRS";
	public static final String EXTRA_ITEM_STATEMENT_SHEET = "EXTRA_ITEM_STATEMENT";
	public static final String EXTRA_ITEM_STATEMENT_SHEET_SHORT = "EIS";
	
	public static String getShortName(String sheet){
		switch (sheet) {
		case MEASUREMENTSHEET:
			return MEASUREMENTSHEET_SHORT;
		case ABSTRACTSHEET:
			return ABSTRACTSHEET_SHORT;
		case RB_SCHEDULE:
			return RB_SCHEDULE_SHORT;
		case FNFB_SCHEDULE:
			return FNFB_SCHEDULE_SHORT;
		case TEMPSHEET:
		 	return TEMPSHEET_SHORT;
		case DEVIATIONSHEET:
		 	return DEVIATION_SHORT;
		case BILLFORMS_MAIN_SHEET:
			return BILLFORMS_MAIN_SHEET_SHORT;
		case PART_RATE_STATEMENT_SHEET:
			return PART_RATE_STATEMENT_SHEET_SHORT;
		case EXTRA_ITEM_STATEMENT_SHEET:
			return EXTRA_ITEM_STATEMENT_SHEET_SHORT;
		default:
			return null;
		}
	}
}
