package com.org.service;

import java.io.InputStream;

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
import com.org.service.blobstore.FileStorageService;
import com.org.util.FileStorageProperties;

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
		XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
		ExcelUtill.saveChangesToDocument(billformsTemplate, workbook);
	}
}
