package com.org.excel.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ItemRanges {
	public static String ID = "ID";

	public static String ITEM_NUM = "ITEM_NUM";

	public static String DESC = "DESC";

	public static String QTY = "QTY";

	public static String UNIT = "UNIT";

	public static String QTY_PER_UNIT = "QTY_PER_UNIT";

	public static String DSR_RATE = "DSR_RATE";

	public static String FULL_RATE = "FULL_RATE";

	public static String PART_RATE = "PART_RATE";

	public static String DSR_CODE = "DSR_CODE";

	public static String T_AGGREEMENT_ID = "T_AGGREEMENT_ID";

	public static String ITEM_PREFIX = "TABLE_ITEMS_";

	public static String EXTRA_ITEM_PREFIX = "TABLE_EXTRA_ITEMS_";

	public static String T_METADATA_EXISTING_ITEMS_IN_DATABASE = "EXISTING_ITEMS_IN_DATABASE";

	public Integer idCol;

	public Integer validItemNumCol;

	public Integer itemNumCol;

	public Integer descCol;

	public Integer qtyCol;

	public Integer unitCol;

	public Integer qtyPerUnitCol;

	public Integer dsrRateCol;

	public Integer fullRateCol;

	public Integer partRateCol;

	public Integer dsrCodeCol;

	private XSSFWorkbook workbook;
	
	private boolean extraItem;

	public ItemRanges(XSSFWorkbook workbook, boolean extraItem) {
		this.workbook = workbook;
		this.extraItem = extraItem;
	}

	public Integer getIdCol() {
		if (idCol == null) {
			idCol = (new XLColumnRange(workbook, withPrefix(ID))).getFirstColNum();
		}
		return idCol;
	}

	public Integer getItemNumCol() {
		if (itemNumCol == null) {
			itemNumCol = (new XLColumnRange(workbook, withPrefix(ITEM_NUM)))
					.getFirstColNum();
		}
		return itemNumCol;
	}

	public Integer getDescCol() {
		if (descCol == null) {
			descCol = (new XLColumnRange(workbook, withPrefix(DESC)))
					.getFirstColNum();
		}
		return descCol;
	}

	public Integer getQtyCol() {
		if (qtyCol == null) {
			qtyCol = (new XLColumnRange(workbook, withPrefix(QTY)))
					.getFirstColNum();
		}
		return qtyCol;
	}

	public Integer getUnitCol() {
		if (unitCol == null) {
			unitCol = (new XLColumnRange(workbook, withPrefix(UNIT)))
					.getFirstColNum();
		}
		return unitCol;
	}

	public Integer getQtyPerUnitCol() {
		if (qtyPerUnitCol == null) {
			qtyPerUnitCol = (new XLColumnRange(workbook, withPrefix(QTY_PER_UNIT)))
					.getFirstColNum();
		}
		return qtyPerUnitCol;
	}

	public Integer getDsrRateCol() {
		if (dsrRateCol == null) {
			dsrRateCol = (new XLColumnRange(workbook, withPrefix(DSR_RATE)))
					.getFirstColNum();
		}
		return dsrRateCol;
	}

	public Integer getFullRateCol() {
		if (fullRateCol == null) {
			fullRateCol = (new XLColumnRange(workbook, withPrefix(FULL_RATE)))
					.getFirstColNum();
		}
		return fullRateCol;
	}

	public Integer getPartRateCol() {
		if (partRateCol == null) {
			partRateCol = (new XLColumnRange(workbook, withPrefix(PART_RATE)))
					.getFirstColNum();
		}
		return partRateCol;
	}

	public Integer getDsrCodeCol() {
		if (dsrCodeCol == null) {
			dsrCodeCol = (new XLColumnRange(workbook, withPrefix(DSR_CODE)))
					.getFirstColNum();
		}
		return dsrCodeCol;
	}

	private String withPrefix(String name){
		if(this.extraItem == true){
			return EXTRA_ITEM_PREFIX + name;
		}else{
			return ITEM_PREFIX + name;
		}
	}
}
