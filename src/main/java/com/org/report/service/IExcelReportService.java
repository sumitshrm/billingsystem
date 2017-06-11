package com.org.report.service;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.org.entity.MeasurementSheet;

public interface IExcelReportService {
	public void generateMasterData(MeasurementSheet msheet, XSSFWorkbook wb) throws Exception;
	public void generateReport(MeasurementSheet msheet, XSSFWorkbook workbook) throws Exception;
}
