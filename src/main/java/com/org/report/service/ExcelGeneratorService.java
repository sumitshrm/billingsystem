package com.org.report.service;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.org.constants.Worksheets;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.XLColumnRange;

public abstract class ExcelGeneratorService {

	public ExcelGeneratorService() {
		super();
	}
	
	public XSSFCell writeMasterData(XSSFSheet xsheet, String cellName, Object value) throws Exception{
		String actualCellName = Worksheets.getShortName(xsheet.getSheetName())+"_"+cellName;
		XLColumnRange range = new XLColumnRange(xsheet.getWorkbook(), actualCellName);
		XSSFCell cell = range.fetchSingleCell();
		ExcelUtill.writeCellValue(value, cell);
		return cell;
	}
	
	

}