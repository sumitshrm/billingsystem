package com.org.constants;

public interface MeasurementSheetConstants {
	String NAME_OF_WORK = "<NAME_OF_WORK>";
	String AGENCY = "<AGENCY>";
	String AGGREEMENT_NO = "<AGGREEMENT_NO>";
	String DATE_OF_START = "<DATE_OF_START>";
	String DATE_OF_COMPLETION_S = "<DATE_OF_COMPLETION_S>";
	String DATE_OF_COMPLETION_A = "<DATE_OF_COMPLETION_A>";
	String SNO_OF_BILL = "<SNO_OF_BILL>";
	String CLAUSE = "<CLAUSE>";
	String DATE_OF_ABSTRACT = "<DATE_OF_ABSTRACT>";
	String TENDER_COST = "<TENDER_COST>";
	String ESTIMATED_COST = "<ESTIMATED_COST>";
	public String ITEM_DESC_VLOOKUP_CELL_IDENTIFIER = "#REF";
	public String ITEM_DESC_RANGE = Worksheets.ITEMS_SHEET + "!A1:B200";
	public String MEASUREMENT_TITLES_RANGE_NAME = "MEASUREMENT_TITLES";
	public String MEASUREMENT_PAGE_NUMBER = "MEASUREMENT_PAGE_NUMBER";
	public String MEASUREMENT_ABSTRACT_REF = "MEASUREMENT_ABSTRACT_REF";
	public String TEMPLATE_MEASUREMENT_SHEET_ID = "T_MEASUREMENT_SHEET_ID";
	public String TEMPLATE_EXISTING_ITEMS_IN_DATABASE = "EXISTING_ITEMS_IN_DATABASE";
	public String TEMPLATE_EXTRA_ITEM_COUNTER = "T_EXTRA_ITEM_COUNTER";
	public String TEMPLATE_T_AGGREEMENT_ID = "T_AGGREEMENT_ID";
	public String MEASUREMENT_TITLES_METADATA_NAME = "metadata";
	public String M_ITEM_NUM = "M_ITEM_NUM";
	public String CD_ITEM_NUM = "CD_ITEM_NUM";
	public String CD_TOTAL = "CD_TOTAL";
	public String CD_A_PAGE = "CD_A_PAGE";
	public String CD_M_PAGE = "CD_M_PAGE";
	
	public String CD_ABSTRACT = "CD_ABSTRACT";
	public String EXTRA_ITEM_DESC_RANGE = Worksheets.EXTRA_ITEMS_SHEET
			+ "!A1:B200";
	public String ITEM_DESC_VLOOKUP_FORMULA = "IFERROR(VLOOKUP("
			+ ITEM_DESC_VLOOKUP_CELL_IDENTIFIER + "," + ITEM_DESC_RANGE
			+ ",2,FALSE), IFERROR(VLOOKUP(" + ITEM_DESC_VLOOKUP_CELL_IDENTIFIER
			+ "," + EXTRA_ITEM_DESC_RANGE + ",2,FALSE), \" \"))";

}
