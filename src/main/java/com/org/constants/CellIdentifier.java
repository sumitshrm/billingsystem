package com.org.constants;

public interface CellIdentifier {
	public String ITEM_NUMBER_IDENTIFIER = "Aggreement item number";
	public int ITEM_NUMBER_IDENTIFIER_ROW_INDEX = 0;
	public int ITEM_NUMBER_VALUE_ROW_INDEX = 1;
	public String TOTAL_IDENTIFIER = "C/o TMB/ Page No.";
	public int TOTAL_IDENTIFIER_ROW_INDEX = 0;
	public int TOTAL_VALUE_ROW_INDEX = 5;
	public int MEASUREMENT_SHEET_META_DATA_COLUMN = 1;
	public String ABSTRACT_TOTAL_IDENTIFIER_FORMULA_PARAM = "<<REF>>";
	public String ABSTRACT_TOTAL_IDENTIFIER_FORMULA = "CONCATENATE(T_PAGE_NUMBER,"+ABSTRACT_TOTAL_IDENTIFIER_FORMULA_PARAM+")";
	public String PAGE_NUM_IDENTIFIER = "{p}";
	
	}
