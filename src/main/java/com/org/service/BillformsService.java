package com.org.service;

import java.io.InputStream;

import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.constants.Worksheets;
import com.org.entity.Aggreement;
import com.org.entity.BillformsTo;
import com.org.entity.Template;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.MasterDataCellName;
import com.org.excel.util.TemplateType;
import com.org.excel.util.XLColumnRange;
import com.org.service.blobstore.FileStorageService;
import com.org.util.FileStorageProperties;
import com.org.util.NumberToWordConverter;

@Service
public class BillformsService {
	
	@Autowired
	private FileStorageService fileStorageService;
	
	public Template createBillformsDocument(BillformsTo billformsTo) throws Exception{
		Template billformsTemplate = Template.findTemplatesByType(TemplateType.BILLFORMS).getSingleResult();
		this.writeBillformData(billformsTemplate, billformsTo);
		return billformsTemplate;
	}
	
	private void removeSheets(BillformsTo billformsTo, XSSFWorkbook workbook) {
		for(String sheetName : billformsTo.getReports()) {
			if(!billformsTo.getSelectedReports().contains(sheetName)) {
				workbook.removeSheetAt(workbook.getSheetIndex(sheetName));
			}
		}
	}
	
	private void writeBillformData(Template billformsTemplate, BillformsTo billformsTo) throws Exception{
		InputStream is = fileStorageService.doGet(FileStorageProperties.BILLFORMS_TEMPLATE);
		XSSFWorkbook workbook = new XSSFWorkbook(is);
		removeSheets(billformsTo, workbook);
		XSSFSheet xsheet = workbook.getSheet(Worksheets.BILLFORMS_MAIN_SHEET);
		Aggreement aggreement = billformsTo.getAggreement();
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.AGGREEMENT_NO, aggreement.getAggreementNum());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.NAME_OF_WORK, aggreement.getNameOfWork());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.AGENCY, aggreement.getAgency());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_START, aggreement.getDateOfStart());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_COMPLETION_S, aggreement.getDateOfCompletionS());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_COMPLETION_A, aggreement.getDateOfCompletionA());
		//ExcelUtill.writeMasterData(xsheet, MasterDataCellName.SNO_OF_BILL, msheet.getSerialNumberDisplayFormat());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.CLAUSE, aggreement.getClausePercentage() + "% " + aggreement.getClause());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.TENDER_COST, aggreement.getTenderCost());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.ESTIMATED_COST, aggreement.getEstimatedCost()); 
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DIVISION, aggreement.getDivision()); 
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.SUB_DIVISION, aggreement.getSubDivision());
		XLColumnRange range = new XLColumnRange(xsheet.getWorkbook(), BillformsTo.BF_SNO_OF_BILL);
		ExcelUtill.writeCellValue(billformsTo.getSerialNumber(), range.fetchSingleCell());
		
		range = new XLColumnRange(xsheet.getWorkbook(), BillformsTo.BF_SNO_OF_BILL);
		ExcelUtill.writeCellValue(billformsTo.getSerialNumber(), range.fetchSingleCell());
		
		range = new XLColumnRange(xsheet.getWorkbook(), BillformsTo.BF_PREV_AMOUNT);
		ExcelUtill.writeCellValue(billformsTo.getUptoPreviousBill(), range.fetchSingleCell());
		
		range = new XLColumnRange(xsheet.getWorkbook(), BillformsTo.BF_TOTAL_AMOUNT);
		ExcelUtill.writeCellValue(billformsTo.getTotalAmount(), range.fetchSingleCell());
		
		range = new XLColumnRange(xsheet.getWorkbook(), BillformsTo.BF_SIGN_1);
		ExcelUtill.writeCellValue(billformsTo.getSignature1(), range.fetchSingleCell());
		
		range = new XLColumnRange(xsheet.getWorkbook(), BillformsTo.BF_SIGN_2);
		ExcelUtill.writeCellValue(billformsTo.getSignature2(), range.fetchSingleCell());
		
		range = new XLColumnRange(xsheet.getWorkbook(), BillformsTo.BF_SIGN_3);
		ExcelUtill.writeCellValue(billformsTo.getSignature3(), range.fetchSingleCell());
		
		range = new XLColumnRange(xsheet.getWorkbook(), BillformsTo.BF_SIGN_4);
		ExcelUtill.writeCellValue(billformsTo.getSignature4(), range.fetchSingleCell());
		
		if(billformsTo.getTotalAmount()!=null && billformsTo.getUptoPreviousBill()!=null) {
			int amount = billformsTo.getTotalAmount() - billformsTo.getUptoPreviousBill();
			range = new XLColumnRange(xsheet.getWorkbook(), BillformsTo.BF_AMOUNT_IN_WORDS);
			ExcelUtill.writeCellValue(NumberToWordConverter.convert(amount), range.fetchSingleCell());
			System.out.println(NumberToWordConverter.convert(amount));
		}
		
		XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
		ExcelUtill.saveChangesToDocument(billformsTemplate, workbook);
	}
}
