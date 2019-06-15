package com.org.report.service;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.org.constants.Worksheets;
import com.org.entity.Item;
import com.org.entity.ItemAbstract;
import com.org.entity.MeasurementSheet;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.DeviationRanges;
import com.org.excel.util.MasterDataCellName;
import com.org.excel.util.PartRateStatementRanges;
import com.org.excel.util.XLColumnRange;

@Service
public class PartRateStatementGeneratorService implements IExcelReportService{

	final static Logger logger = Logger.getLogger(PartRateStatementGeneratorService.class);

	@Override
	public void generateMasterData(MeasurementSheet msheet, XSSFWorkbook wb)
			throws Exception {
		System.out.println("writing part rate statement data");
		XSSFSheet xsheet = wb.getSheet(Worksheets.PART_RATE_STATEMENT_SHEET);
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.AGGREEMENT_NO, msheet.getAggreement().getAggreementNum());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.NAME_OF_WORK, msheet.getAggreement().getNameOfWork());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.AGENCY, msheet.getAggreement().getAgency());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_START, msheet.getAggreement().getDateOfStart());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_COMPLETION_S, msheet.getAggreement().getDateOfCompletionS());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.TENDER_COST, msheet.getAggreement().getTenderCost());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.ESTIMATED_COST, msheet.getAggreement().getEstimatedCost()); 
	}
	
	public void removeReportData(MeasurementSheet msheet, XSSFWorkbook wb){
		XSSFSheet xsheet = wb.getSheet(Worksheets.PART_RATE_STATEMENT_SHEET);
		PartRateStatementRanges ranges = new PartRateStatementRanges(wb);
		ExcelUtill.deleteRow(xsheet, ranges.getrSlno().getFirstRowNum()+2, xsheet.getLastRowNum());
	}

	@Override
	public void generateReport(MeasurementSheet msheet, XSSFWorkbook workbook)
			throws Exception {
		try {
			removeReportData(msheet, workbook);
			PartRateStatementRanges ranges = new PartRateStatementRanges(workbook);
			XSSFSheet xsheet = workbook.getSheet(Worksheets.PART_RATE_STATEMENT_SHEET);
			int slno = ranges.getrSlno().getFirstColNum();
			int descriptionOfItem = ranges.getrDescriptionOfItem().getFirstColNum();
			int fullRate = ranges.getrFullRate().getFirstColNum();
			int partRate = ranges.getrPartRate().getFirstColNum();
			int remarksCol = ranges.getrRemarks().getFirstColNum();
			XSSFRow row  = null;
			int currRow = ranges.getrSlno().getLastRowNum()+2;
			XSSFCellStyle descriptionStyle = ExcelUtill.getBoxStyle(workbook);
			descriptionStyle.setAlignment(HorizontalAlignment.JUSTIFY);
			XSSFCellStyle boxStyle = ExcelUtill.getBoxStyle(workbook);
			for(Item item : msheet.getAggreement().getItems()){
				try {
					//commented below line as discussed. write part rate report for all items irrespective of fullrate!=partrate
					//if(!isItemWritable(item)){continue;}
					if(item.isValidItem()){
						row = xsheet.createRow(currRow++);
						writeEntryForItem(slno, descriptionOfItem, fullRate, partRate,
								remarksCol, row, descriptionStyle, boxStyle, item);
					}else{
						row = xsheet.createRow(currRow++);
						writeDescriptionForParentItem(slno, descriptionOfItem, fullRate, partRate,
								remarksCol, row, descriptionStyle, boxStyle, item);
					}
				
				} catch (Exception e) {
					logger.error("error writing part rate statement for item: "+item.getItemNumber()+"\r\n"+e.getMessage(), e);
					throw new Exception("error writing part rate statement for item: "+item.getItemNumber()+"\r\n"+e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			logger.error("error writing part rate statement : \r\n"+e.getMessage(), e);
			throw new Exception("error writing part rate statement : \r\n"+e.getMessage(), e);
		}
	}
	
	private boolean isItemWritable(Item item){
		if(item.getSubItems()==null){
			return isPartRateAndFullRateNotEqual(item);
		}else{
			for(Item subItem : item.getSubItems()){
				if(isItemWritable(subItem)){
					return true;
				}
			}
		}
		return isPartRateAndFullRateNotEqual(item);
	}
	
	private boolean isPartRateAndFullRateNotEqual(Item item){
		if(item.getPartRate()!=null && item.getFullRate()!=null && !item.getPartRate().equals(item.getFullRate())){
			return true;
		}
		return false;
	}

	private void writeEntryForItem(int slno, int descriptionOfItem,
			int fullRate, int partRate, int remarksCol, XSSFRow row,
			XSSFCellStyle descriptionStyle, XSSFCellStyle boxStyle, Item item)
			throws Exception {
		/*if(item.getPartRate()==item.getFullRate()){
			return;
		}*/
		ExcelUtill.writeCellValue(item.getItemNumExcelFormat(), row.createCell(slno), boxStyle);
		ExcelUtill.writeCellValue(item.getDescription(), row.createCell(descriptionOfItem), descriptionStyle);
		ExcelUtill.writeCellValue(item.getFullRate(), row.createCell(fullRate), boxStyle);
		ExcelUtill.writeCellValue(item.getPartRate(), row.createCell(partRate), boxStyle);
		ExcelUtill.writeCellValue("", row.createCell(remarksCol), boxStyle);
	}
	
	private void writeDescriptionForParentItem(int slno, int descriptionOfItem,
			int fullRate, int partRate, int remarksCol, XSSFRow row,
			XSSFCellStyle descriptionStyle, XSSFCellStyle boxStyle, Item item)
			throws Exception {
		ExcelUtill.writeCellValue(item.getItemNumExcelFormat(), row.createCell(slno), boxStyle);
		ExcelUtill.writeCellValue(item.getDescription(), row.createCell(descriptionOfItem), descriptionStyle);
		ExcelUtill.writeCellValue("", row.createCell(fullRate), boxStyle);
		ExcelUtill.writeCellValue("", row.createCell(partRate), boxStyle);
		ExcelUtill.writeCellValue("", row.createCell(remarksCol), boxStyle);
	}
	
	

}
