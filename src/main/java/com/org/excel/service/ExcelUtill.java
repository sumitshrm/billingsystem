package com.org.excel.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.org.constants.Worksheets;
import com.org.entity.Document;
import com.org.entity.IDocument;
import com.org.entity.Template;
import com.org.excel.util.XLColumnRange;

public class ExcelUtill {

	private static DataFormatter formatter = new DataFormatter();
	
	public static void setDateValue(Date date, Cell cell) {
		if (date == null) {
			cell.setCellValue("");
		} else {

			CreationHelper createHelper = cell.getSheet().getWorkbook()
					.getCreationHelper();
			cell.getCellStyle().setDataFormat(
					createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
			cell.setCellValue(date);
		}

	}

	public static void setCellDoubleValue(Cell cell, Double value) {
		if (value != null) {
			cell.setCellValue(value);
		}
	}

	public static void removeSheetByName(XSSFWorkbook wb, String sheetName) {
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			if (wb.getSheetAt(i).getSheetName().equals(sheetName)) {
				wb.removeSheetAt(i);
				break;
			}
		}
	}

	public static XSSFCellStyle getBoxStyle(XSSFWorkbook wb) {
		XSSFCellStyle style = wb.createCellStyle();
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		return style;
	}
	
	public static XSSFCellStyle getBoldBoxStyle(XSSFWorkbook wb) {
		XSSFCellStyle style = getBoxStyle(wb);
		XSSFFont boldFont= wb.createFont();
		boldFont.setBold(true);
		style.setFont(boldFont);
		return style;
	}
	
	public static XSSFCellStyle getBorderTopRight(XSSFWorkbook wb) {
		XSSFCellStyle style = getBoxStyle(wb);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setVerticalAlignment(VerticalAlignment.TOP);
		return style;
	}
	
	public static XSSFCellStyle getAbstractDescriptionStyle(XSSFWorkbook wb) {
		XSSFCellStyle style = getBoxStyle(wb);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setWrapText(true);
		style.setAlignment(HorizontalAlignment.JUSTIFY);
		style.setVerticalAlignment(VerticalAlignment.TOP);
		return style;
	}
	
	public static XSSFCellStyle getBorderTop(XSSFWorkbook wb) {
		XSSFCellStyle style = wb.createCellStyle();
		style.setBorderTop(CellStyle.BORDER_THIN);
		return style;
	}
	
	public static XSSFCellStyle getBoldFont(XSSFWorkbook wb) {
		XSSFCellStyle style = wb.createCellStyle();
		XSSFFont boldFont= wb.createFont();
		boldFont.setBold(true);
		style.setFont(boldFont);
		return style;
	}
	
	public static XSSFCellStyle setBoldFont(XSSFWorkbook wb, XSSFCellStyle style) {
		XSSFFont boldFont= wb.createFont();
		boldFont.setBold(true);
		style.setFont(boldFont);
		return style;
	}
	
	public static XSSFCellStyle setPercentFormat(XSSFWorkbook wb, XSSFCellStyle style){
		CreationHelper createHelper = wb.getCreationHelper();
		style.setDataFormat(
		    createHelper.createDataFormat().getFormat("0.00\" %\""));
		return style;
	}
	
	public static void setCellFormat(XSSFCell cell, String format){
		XSSFWorkbook wb = cell.getSheet().getWorkbook();
		XSSFCellStyle style = wb.createCellStyle();
		DataFormat dataFormat = wb.createDataFormat();
		style.setDataFormat(dataFormat.getFormat(format));
		cell.setCellStyle(style);
	}

	public static void saveChanges(Document document)
			throws IOException {
		System.out.println("saving changes.....");
		try {
			OutputStream out = new FileOutputStream(new File(document.getUrl()));
			try {

				out.write(document.getContent());
				out.close();
			} catch (IOException e) {
				throw new IOException();
			} finally {
				try {
					out.close();
				} catch (IOException e2) {
					throw e2;
				}
			}
		} catch (FileNotFoundException e) {
			throw e;
		}

		/*
		 * ByteArrayOutputStream bos = new ByteArrayOutputStream(); try {
		 * workbook.write(bos); } catch (IOException e) { throw new
		 * IOException(); } finally { try { bos.close(); } catch (IOException e)
		 * { e.printStackTrace(); } } document.setContent(bos.toByteArray());
		 */
	}
	
	
	public static void saveChangesToDocument(Template document, XSSFWorkbook workbook) {
		System.out.println("saving changes.....");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			workbook.write(bos);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		document.setContent(bos.toByteArray());
	}

	public static String getCellValueAsString(Cell cell) {
		if (cell == null) {
			return "";
		} else {
			FormulaEvaluator evaluator = cell.getSheet().getWorkbook()
					.getCreationHelper().createFormulaEvaluator();
			return formatter.formatCellValue(cell, evaluator);

		}
	}

	public static String getCellValueAsString(CellValue cellValue) {
		switch (cellValue.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			return Boolean.toString(cellValue.getBooleanValue());
		case Cell.CELL_TYPE_NUMERIC:
			return Integer.toString((int) cellValue.getNumberValue());
		case Cell.CELL_TYPE_STRING:
			return cellValue.getStringValue();
		case Cell.CELL_TYPE_BLANK:
			return "";
		case Cell.CELL_TYPE_ERROR:
			return "#ERROR#";
			// CELL_TYPE_FORMULA will never happen
		case Cell.CELL_TYPE_FORMULA:
			return "";
		default:
			return "";
		}
	}

	public static char intToChar(int input) {
		return (char) (input + 65);
	}

	public static void writeCellValue(Object o, Cell cell) throws Exception {
		if(o==null){
			cell.setCellValue("");
		} 
		else if (o != null && o instanceof String) {
			String val = (String) o;
			if (val.startsWith("=")) {
				cell.setCellFormula(val.substring(1));
			} else {
				cell.setCellValue(val);
			}
		} else if (o != null && o instanceof Double) {
			cell.setCellValue((Double) o);
			CellStyle cs = cell.getCellStyle();
			cs.setDataFormat(cell.getSheet().getWorkbook().createDataFormat().getFormat("0.00"));
			cell.setCellStyle(cs);
		} else if (o != null && o instanceof Integer) {
			cell.setCellValue((Integer) o);
		} else if (o != null && o instanceof Date){
			setDateValue((Date)o, cell);
		} else if (o != null && o instanceof Long){
			cell.setCellValue((Long) o);
		}
		else {
			cell.setCellValue(o.toString());
		}
	}

	public static void writeCellValue(Object o, Cell cell, XSSFCellStyle style)
			throws Exception {
		if (style != null) {
			cell.setCellStyle(style);
		}
		writeCellValue(o, cell);
	}

	public static String getCellReference(Cell cell) {
		CellReference ref = new CellReference(cell);
		return ref.formatAsString();
	}
	
	public static int getColumnNumberByRangeName(XSSFWorkbook wb, String name){
		XLColumnRange range = new XLColumnRange(wb, name);
		return range.getFirstColNum();
	}
	
	public static void writeMasterData(XSSFSheet xsheet, String cellName, Object value) throws Exception{
		String actualCellName = Worksheets.getShortName(xsheet.getSheetName())+"_"+cellName;
		XLColumnRange range = new XLColumnRange(xsheet.getWorkbook(), actualCellName);
		writeCellValue(value, range.fetchSingleCell());
	}
	
	public static void writeIntoCellByName(XSSFSheet xsheet, String cellName, Object value, XSSFCellStyle style){
		//TODO:
	}
	
	public static XSSFWorkbook getWorkbook(Document document) {
		XSSFWorkbook workbook =null;
		try {
			workbook = (XSSFWorkbook) WorkbookFactory.create(new File(document.getUrl()));
		} catch (IOException | InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workbook;
		/*ByteArrayInputStream bais = new ByteArrayInputStream(
				document.getContent());
		XSSFWorkbook workbook = null;
		try { 
			workbook = (XSSFWorkbook) WorkbookFactory.create(bais); 
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bais.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return workbook;*/
	}
	
	public static XSSFWorkbook getWorkbookFromContent(byte[] bytes) {
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		XSSFWorkbook workbook = null;
		try { 
			workbook = (XSSFWorkbook) WorkbookFactory.create(bais); 
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bais.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return workbook;
	}
	
	public static void removeAndShiftRow(XSSFSheet sheet, int rowIndex,
			int lastRowIdex) {
		int extraRow = lastRowIdex + (lastRowIdex - rowIndex);
		// sheet.createRow(extraRow).createCell(0).setCellValue(extraRow);

		int lastRowNum = extraRow;
		int mid = lastRowIdex;
		int shift_count = lastRowNum - mid;
		if (mid >= 0 && mid < lastRowNum) {
			sheet.shiftRows(mid + 1, lastRowNum, -shift_count);
		}

		if (rowIndex == sheet.getLastRowNum()) {
			XSSFRow removingRow = sheet.getRow(sheet.getLastRowNum());
			if (removingRow != null) {
				sheet.removeRow(removingRow);
			}
		}
	}
 
	public static void deleteRow(XSSFSheet sheet, int rowIndex, int lastRowIdex) {
		for (int x = rowIndex; x <= lastRowIdex; x++) {
			XSSFRow row = sheet.getRow(x);
			if (row != null) {
				sheet.removeRow(row);
			}
		}
	}
	
	public static String getFileNameWithTimeStamp(String originalName){
		String[] temp = originalName.trim().split("\\.");
		String prefix;
		String postfix;
		if(temp.length>1){
			prefix = temp[0] + (System.currentTimeMillis());
			postfix = temp[1];
			return prefix + "." + postfix;
		}
		return originalName;
	}

}
