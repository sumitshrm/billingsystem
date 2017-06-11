package com.org.excel.util;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.org.constants.AbstractSheetConstants;
import com.org.excel.service.ExcelUtill;

public class AbstractRanges {
	
	private XSSFWorkbook workbook;

	private XLColumnRange rItemNumber;

	private XLColumnRange rDescription;

	private XLColumnRange rQuantity;

	private XLColumnRange rUnit;

	private XLColumnRange rFullRate;

	private XLColumnRange rPartRate;
	
	private XLColumnRange rAmount;
	
	private XLColumnRange rMetadataPageNum;
	
	private XLColumnRange rMetadataItemNum;
	
	private XLColumnRange rMetadataSubItemNum;
	
	private XLColumnRange rMetaDataPartRateUpdated;
	
	private XLColumnRange rMetaDataTotal;
	
	private XSSFCellStyle boldStyle;

	private XSSFCellStyle boxStyle;
	
	private XSSFCellStyle boxAndBoldStyle;
	
	private XSSFCellStyle borderTopRightStyle;
	
	private XSSFCellStyle descriptionCellStyle;
	//Write item number and description in header row.
	
	
	private Integer itemNumCol;

	
	private Integer descCol;

	private Integer qtyCol;

	private Integer unitCol;

	private Integer fullRateCol;

	private Integer partRateCol;
	
	private Integer amountCol;
	
	private Integer metadataPageNumCol;
	
	private Integer metadataItemNumCol;
	
	private Integer metadataSubItemNumCol;
	
	private Integer metaDataPartRateUpdatedCol;
	
	private Integer metaDataTotalCol;

	public AbstractRanges(XSSFWorkbook workbook) {
		super();
		this.workbook = workbook;
		this.rItemNumber = new XLColumnRange(workbook, AbstractSheetConstants.RANGE_ABSTRACT_ITEM_NUM);
		this.rDescription = new XLColumnRange(workbook, AbstractSheetConstants.RANGE_ABSTRACT_DESCRIPTION);
		this.rQuantity = new XLColumnRange(workbook, AbstractSheetConstants.RANGE_ABSTRACT_QUANTITY);
		this.rUnit = new XLColumnRange(workbook, AbstractSheetConstants.RANGE_ABSTRACT_UNIT);
		this.rFullRate = new XLColumnRange(workbook, AbstractSheetConstants.RANGE_ABSTRACT_FULL_RATE);
		this.rPartRate = new XLColumnRange(workbook, AbstractSheetConstants.RANGE_ABSTRACT_PART_RATE);
		this.rAmount = new XLColumnRange(workbook, AbstractSheetConstants.RANGE_ABSTRACT_AMOUNT);
		this.rMetadataPageNum = new XLColumnRange(workbook, AbstractSheetConstants.RANGE_A_METADATA_PAGE_NUM);
		this.rMetadataItemNum = new XLColumnRange(workbook, AbstractSheetConstants.RANGE_A_METADATA_ITEM_NUM);
		this.rMetaDataPartRateUpdated = new XLColumnRange(workbook, AbstractSheetConstants.RANGE_A_METADATA_PART_RATE_UPDATED);
		this.rMetadataSubItemNum = new XLColumnRange(workbook, AbstractSheetConstants.RANGE_A_METADATA_SUB_ITEM_NUM);
		this.rMetaDataTotal = new XLColumnRange(workbook, AbstractSheetConstants.RANGE_A_METADATA_TOTAL);
	}

	
	public XLColumnRange getrItemNumber() {
		return rItemNumber;
	}

	public XLColumnRange getrDescription() {
		return rDescription;
	}

	public XLColumnRange getrQuantity() {
		return rQuantity;
	}

	public XLColumnRange getrUnit() {
		return rUnit;
	}

	public XLColumnRange getrFullRate() {
		return rFullRate;
	}

	public XLColumnRange getrPartRate() {
		return rPartRate;
	}

	public XLColumnRange getrAmount() {
		return rAmount;
	}

	public XLColumnRange getrMetadataPageNum() {
		return rMetadataPageNum;
	}

	public void setrMetadataPageNum(XLColumnRange rPageNum) {
		this.rMetadataPageNum = rPageNum;
	}


	public XLColumnRange getrMetadataItemNum() {
		return rMetadataItemNum;
	}


	public void setrMetadataItemNum(XLColumnRange rMetadataItemNum) {
		this.rMetadataItemNum = rMetadataItemNum;
	}


	public XLColumnRange getrMetadataSubItemNum() {
		return rMetadataSubItemNum;
	}


	public void setrMetadataSubItemNum(XLColumnRange rMetadataSubItemNum) {
		this.rMetadataSubItemNum = rMetadataSubItemNum;
	}


	public XLColumnRange getrMetaDataPartRateUpdated() {
		return rMetaDataPartRateUpdated;
	}


	public void setrMetaDataPartRateUpdated(XLColumnRange rMetaDataPartRateUpdated) {
		this.rMetaDataPartRateUpdated = rMetaDataPartRateUpdated;
	}


	public XLColumnRange getrMetaDataTotal() {
		return rMetaDataTotal;
	}


	public void setrMetaDataTotal(XLColumnRange rMetaDataTotal) {
		this.rMetaDataTotal = rMetaDataTotal;
	}
	
	public Integer getItemNumCol() {
		if(itemNumCol==null){
			itemNumCol = rItemNumber.getFirstColNum();
		}
		return itemNumCol;
	}


	public Integer getDescCol() {
		if(descCol==null){
			descCol = rDescription.getFirstColNum();
		}
		return descCol;
	}


	public Integer getQtyCol() {
		if(qtyCol==null){
			qtyCol = rQuantity.getFirstColNum();
		}
		return qtyCol;
	}


	public Integer getUnitCol() {
		if(unitCol==null){
			unitCol = rUnit.getFirstColNum();
		}
		return unitCol;
	}


	public Integer getFullRateCol() {
		if(fullRateCol==null){
			fullRateCol = rFullRate.getFirstColNum();
		}
		return fullRateCol;
	}


	public Integer getPartRateCol() {
		if(partRateCol==null){
			partRateCol=rPartRate.getFirstColNum();
		}
		return partRateCol;
	}


	public Integer getAmountCol() {
		if(amountCol==null){
			amountCol=rAmount.getFirstColNum();
		}
		return amountCol;
	}


	public Integer getMetadataPageNumCol() {
		if(metadataPageNumCol==null){
			metadataPageNumCol=rMetadataPageNum.getFirstColNum();
		}
		return metadataPageNumCol;
	}


	public Integer getMetadataItemNumCol() {
		if(metadataItemNumCol==null){
			metadataItemNumCol=rMetadataItemNum.getFirstColNum();
		}
		return metadataItemNumCol;
	}


	public Integer getMetadataSubItemNumCol() {
		if(metadataSubItemNumCol==null){
			metadataSubItemNumCol=rMetadataSubItemNum.getFirstColNum();
		}
		return metadataSubItemNumCol;
	}


	public Integer getMetaDataPartRateUpdatedCol() {
		if(metaDataPartRateUpdatedCol==null){
			metaDataPartRateUpdatedCol=rMetaDataPartRateUpdated.getFirstColNum();
		}
		return metaDataPartRateUpdatedCol;
	}


	public Integer getMetaDataTotalCol() {
		if(metaDataTotalCol==null){
			metaDataTotalCol = rMetaDataTotal.getFirstColNum();
		}
		return metaDataTotalCol;
	}

	public XSSFCellStyle getBoldStyle() {
		if(boldStyle==null){
			boldStyle = ExcelUtill.getBoldFont(workbook);
		}
			
		return boldStyle;
	}


	public XSSFCellStyle getBoxStyle() {
		if(boxStyle==null){
			boxStyle = ExcelUtill.getBoxStyle(workbook);
		}
		return boxStyle;
	}


	public XSSFCellStyle getBoxAndBoldStyle() {
		if(boxAndBoldStyle==null){
			boxAndBoldStyle = ExcelUtill
					.getBoldBoxStyle(workbook);
		}
		return boxAndBoldStyle;
	}


	public XSSFCellStyle getDescriptionCellStyle() {
		if(descriptionCellStyle==null){
			descriptionCellStyle = ExcelUtill.getAbstractDescriptionStyle(workbook);
		}
		return descriptionCellStyle;
	}


	public XSSFCellStyle getBorderTopRightStyle() {
		if(borderTopRightStyle==null){
			borderTopRightStyle = ExcelUtill.getBorderTopRight(workbook);
		}
		return borderTopRightStyle;
	}

	
	

}
