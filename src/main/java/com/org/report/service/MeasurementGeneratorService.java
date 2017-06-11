package com.org.report.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.org.constants.MeasurementSheetConstants;
import com.org.constants.Worksheets;
import com.org.domain.Config;
import com.org.entity.Item;
import com.org.entity.MeasurementSheet;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.MasterDataCellName;
import com.org.excel.util.XLColumnRange;
import com.org.util.QueryUtil;

@Service
public class MeasurementGeneratorService{

	public void generateMasterData(MeasurementSheet msheet, XSSFWorkbook wb) throws Exception {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		System.out.println(request.getSession().getServletContext().getContextPath()+":ppppppppp");
		System.out.println("writing measurement sheet master data");
		XSSFSheet xsheet = wb.getSheet(Worksheets.MEASUREMENTSHEET);
		XSSFSheet templatesheet = wb.getSheet(Worksheets.TEMPSHEET);
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.AGGREEMENT_NO, msheet.getAggreement().getAggreementNum());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.NAME_OF_WORK, msheet.getAggreement().getNameOfWork());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.AGENCY, msheet.getAggreement().getAgency());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_START, msheet.getAggreement().getDateOfStart());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_COMPLETION_S, msheet.getAggreement().getDateOfCompletionS());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_COMPLETION_A, msheet.getAggreement().getDateOfCompletionA());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.SNO_OF_BILL, msheet.getSerialNumberDisplayFormat());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.CLAUSE, msheet.getAggreement().getClausePercentage() + "% " + msheet.getAggreement().getClause());
		//ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_ABSTRACT, msheet.getAggreement().getDateOfAbstract());
		ExcelUtill.writeMasterData(templatesheet, MasterDataCellName.T_MEASUREMENT_SHEET_ID, msheet.getId());
		//Write template data.
		XSSFCell extraItemCounter_cell = new XLColumnRange(wb, MeasurementSheetConstants.TEMPLATE_EXTRA_ITEM_COUNTER).fetchSingleCell();
		//XSSFCell existingItemCounter_cell = new XLColumnRange(wb, MeasurementSheetConstants.TEMPLATE_EXISTING_ITEMS_IN_DATABASE).fetchSingleCell();
		XSSFCell aggreementId_cell = new XLColumnRange(wb, MeasurementSheetConstants.TEMPLATE_T_AGGREEMENT_ID).fetchSingleCell();
		long extraItemCounter = Item.countFindItemsByAggreementAndIsExtraItemAndParentItemIsNull(msheet.getAggreement(), true);
		ExcelUtill.writeCellValue(extraItemCounter, extraItemCounter_cell);
		//ExcelUtill.writeCellValue(existingItemCounter, existingItemCounter_cell);
		ExcelUtill.writeCellValue(msheet.getAggreement().getId(), aggreementId_cell);
		List<Config> configs = Config.findAllConfigs();
		for(Config config : configs){
			try {
				ExcelUtill.writeCellValue(config.getValue(), (new XLColumnRange(wb, config.getCellName())).fetchSingleCell());
			} catch (Exception e) {
				System.out.println("WARNING : "+e.getMessage());
			}
		}
	}
	
}
