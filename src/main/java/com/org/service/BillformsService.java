package com.org.service;

import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.org.constants.Worksheets;
import com.org.entity.Aggreement;
import com.org.entity.Template;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.MasterDataCellName;
import com.org.excel.util.TemplateType;

@Service
public class BillformsService {

	public Template createBillformsDocument(Aggreement aggreement) throws Exception{
		Template billformsTemplate = Template.findTemplatesByType(TemplateType.BILLFORMS).getSingleResult();
		this.writeBillformData(billformsTemplate, aggreement);
		return billformsTemplate;
	}
	
	private void writeBillformData(Template billformsTemplate, Aggreement aggreement) throws Exception{
		XSSFWorkbook workbook = ExcelUtill.getWorkbookFromContent(billformsTemplate.getContent());
		XSSFSheet xsheet = workbook.getSheet(Worksheets.BILLFORMS_MAIN_SHEET);
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
