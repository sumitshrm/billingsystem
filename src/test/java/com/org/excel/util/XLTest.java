package com.org.excel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XLTest {

	XSSFWorkbook workbook;
	
	protected void init() throws IOException  {
		FileInputStream file = new FileInputStream(new File(
				"D:\\personel\\SUM-InfoTech\\MeasurementTemplate.xlsm"));
		this.workbook = new XSSFWorkbook(file);
	}
	
	protected void init(String path) throws IOException {
		FileInputStream file = new FileInputStream(new File(path));
		this.workbook = new XSSFWorkbook(file);
	}
}
