package com.org.excel.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.org.constants.MeasurementSheetConstants;

public class XLColumnRange {

	private XSSFWorkbook workbook;

	private String name;
	
	private AreaReference areaReference;

	public XLColumnRange(XSSFWorkbook workbook, String name) {
		this.workbook = workbook;
		this.name = name;
	}

	public AreaReference getAreaReference() {
		if(this.areaReference==null){
			int namedCellIdx = workbook.getNameIndex(name);
			Name aNamedCell = workbook.getNameAt(namedCellIdx);
			this.areaReference = new AreaReference(aNamedCell.getRefersToFormula());
		}
		return this.areaReference;
	}

	public CellReference[] getCellReference() {
		return getAreaReference().getAllReferencedCells();
	}

	public XSSFCell getCellFromRow(int rownum, int colnum) {
		CellReference[] crefs = getCellReference();
		if(crefs.length>0){
			XSSFSheet sheet = workbook.getSheet(crefs[0].getSheetName());
			return sheet.getRow(rownum).getCell(crefs[colnum].getCol() + colnum);
		}
		
		return null;
	}
	
	/**
	 * get cell of given rownum, create new cell in case get return
	 * @param rownum
	 * @param colnum
	 * @return
	 */
	public XSSFCell fetchCellFromRow(int rownum, int colnum) {
		CellReference[] crefs = getCellReference();
		XSSFRow row;
		XSSFCell result = null;
		if(crefs.length>0){
			colnum = crefs[colnum].getCol() + colnum;
			row = workbook.getSheet(crefs[0].getSheetName()).getRow(rownum);
			result = row.getCell(colnum)==null?row.createCell(colnum):row.getCell(colnum);
		}
		return result;
	}
	
	/**
	 * get cell of given rownum, create new cell in case get return
	 * @param rownum
	 * @param colnum
	 * @return
	 */
	public XSSFCell fetchSingleCell() {
		int colnum = 0;
		CellReference[] crefs = getCellReference();
		XSSFRow row;
		XSSFCell result = null;
		if(crefs.length>0){
			colnum = crefs[0].getCol();
			row = workbook.getSheet(crefs[0].getSheetName()).getRow(getFirstRowNum());
			result = row.getCell(colnum)==null?row.createCell(colnum):row.getCell(colnum);
		}
		return result;
	}
	
	public int getFirstColNum(){
		return getAreaReference().getFirstCell().getCol();
	}
	
	public int getLastColNum(){
		return getAreaReference().getLastCell().getCol();
	}
	
	public int getFirstRowNum(){
		return getAreaReference().getFirstCell().getRow();
	}
	
	public int getLastRowNum(){
		return getAreaReference().getLastCell().getRow();
	}

}
