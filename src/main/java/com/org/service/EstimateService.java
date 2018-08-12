package com.org.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.deploy.model.XpathListener;
import javax.transaction.Transactional;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.XLSBUnsupportedException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.org.constants.Worksheets;
import com.org.entity.Estimate;
import com.org.entity.EstimateItem;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.AbstractRanges;
import com.org.excel.util.MasterDataCellName;
import com.org.excel.util.XLColumnRange;
import com.org.service.blobstore.FileStorageService;
import com.org.util.EstimateCounter;
import com.org.util.FileStorageProperties;
import com.org.web.EstimateController;
import com.org.web.ExcelGatewayController;

@Service
public class EstimateService {
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@Transactional
	public void createEstimate(Estimate estimate) throws Exception {
		estimate.persist();
	 	estimate.setUrl(FileStorageProperties.ESTIMATE_FOLDER+"ESTIMATES_"+estimate.getId()+".xlsx");
	 	estimate.merge();
		InputStream inputStream = fileStorageService.doGet(FileStorageProperties.TEMPLATE_FILE);
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet msheet = workbook.getSheet(Worksheets.MEASUREMENTSHEET);
		XSSFSheet asheet = workbook.getSheet(Worksheets.ABSTRACTSHEET);
		// Write name of work
		ExcelUtill.writeMasterData(msheet, MasterDataCellName.NAME_OF_WORK, estimate.getNameOfWork());
		ExcelUtill.writeMasterData(asheet, MasterDataCellName.NAME_OF_WORK, estimate.getNameOfWork());
		Integer a_currentrow = asheet.getLastRowNum();
		Integer m_currentrow = msheet.getLastRowNum();
		EstimateCounter counter = new EstimateCounter(m_currentrow, a_currentrow);
		int msheetDescColNum = new XLColumnRange(workbook, "M_DESC_OF_ITEMS").getFirstColNum();
		int msheetTotalColNum = new XLColumnRange(workbook, "M_TOTAL_QTY").getFirstColNum();
		AbstractRanges abstractRanges = new AbstractRanges(msheet.getWorkbook());

		for(EstimateItem item : estimate.getItems()) {
			writeItemData(msheet, asheet ,item, msheetDescColNum, msheetTotalColNum, abstractRanges, counter);
		}
		
		workbook.write(fileStorageService.getOutputStream(estimate.getUrl()));
		workbook.close();
		
		/*inputStream.close();
		FileOutputStream output_file =new FileOutputStream(new File("test.xlsx")); 
		workbook.write(output_file);
		output_file.close();*/
	}
	
	
	private void writeItemData(XSSFSheet msheet, XSSFSheet asheet, EstimateItem item, int msheetDescColNum,
			int msheetTotalColNum, AbstractRanges abstractRanges, EstimateCounter counter) {
		XSSFRow row = msheet.createRow(counter.nextMsheetCounter());
		
		XSSFCell cell = row.createCell(msheetDescColNum);
		//cell.setCellStyle(abstractRanges.getDescriptionCellStyle());
		msheet.addMergedRegion(new CellRangeAddress(counter.getMsheetCounter(),counter.getMsheetCounter(),msheetDescColNum,msheetTotalColNum+1));
		cell.setCellValue(item.getDesc());
		
		cell = row.createCell(msheetDescColNum-1);
		cell.setCellValue(item.getCode());
		
		if(item.getUnit()!=null && !item.getUnit().equals("")) {
			//row = msheet.createRow(counter.nextMsheetCounter());
			//row = msheet.createRow(counter.nextMsheetCounter());
			counter.nextMsheetCounter();
			row = msheet.createRow(counter.nextMsheetCounter());
			cell = row.createCell(msheetTotalColNum-1);
			cell.setCellStyle(ExcelUtill.getBoldFont(msheet.getWorkbook()));
			cell.setCellValue("Say");
			cell = row.createCell(msheetTotalColNum);
			cell.setCellStyle(ExcelUtill.getBoldFont(msheet.getWorkbook()));
			cell.setCellFormula(getTotalQuantityFormula(msheet, counter.getMsheetCounter(), msheetTotalColNum));
			cell = row.createCell(msheetTotalColNum+1);
			cell.setCellStyle(ExcelUtill.getBoldFont(msheet.getWorkbook()));
			cell.setCellValue(item.getUnit());
			writeAbstractData(asheet, counter, item, CellReference.convertNumToColString(msheetTotalColNum) + (counter.getMsheetCounter()+1), abstractRanges);
		}else {
			//msheet.addMergedRegion(new CellRangeAddress(counter.getMsheetCounter(),counter.getMsheetCounter(),msheetDescColNum,msheetTotalColNum+1));
			writeAbstractData(asheet, counter, item, null, abstractRanges);
		}
		
	}
	
	private void writeAbstractData(XSSFSheet msheet, EstimateCounter counter, EstimateItem item, String msheetRefCell, AbstractRanges abstractRanges) {
		XSSFRow row = msheet.createRow(counter.nextAbstractCounter());
		XSSFCell cell = row.createCell(abstractRanges.getItemNumCol());
		cell.setCellValue(item.getCode());
			
		cell = row.createCell(abstractRanges.getDescCol());
		cell.setCellStyle(abstractRanges.getDescriptionCellStyle());
		cell.setCellValue(item.getDesc());
		
		cell=row.createCell(abstractRanges.getAmountCol()+1);
		cell.setCellStyle(abstractRanges.getBorderTopRightStyle());
		cell.setCellValue("");
		
		if(msheetRefCell!=null) {
			cell=row.createCell(abstractRanges.getQtyCol());
			cell.setCellStyle(abstractRanges.getBoxStyle());
			cell.setCellFormula(Worksheets.MEASUREMENTSHEET+"!"+ msheetRefCell);
			
			cell=row.createCell(abstractRanges.getUnitCol());
			cell.setCellStyle(abstractRanges.getBoxStyle());
			cell.setCellValue(item.getUnit());
			
			cell=row.createCell(abstractRanges.getFullRateCol());
			cell.setCellStyle(abstractRanges.getBoxStyle());
			//cell.setCellValue();
			try {
				ExcelUtill.writeCellValue(Double.parseDouble(item.getRate()), cell);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			cell=row.createCell(abstractRanges.getAmountCol());
			cell.setCellStyle(abstractRanges.getBoxStyle());
			cell.setCellFormula(getAbstractAmountFormula(abstractRanges, counter.getAbstractCounter()));
			
			cell=row.createCell(abstractRanges.getAmountCol()+1);
			cell.setCellStyle(abstractRanges.getBoxStyle());
			cell.setCellValue(item.getCode());
		}
	}
	
	private String getTotalQuantityFormula(XSSFSheet msheet, int rownum, int colnum){
		String firstCol = CellReference.convertNumToColString(colnum) + (rownum-1);
		String secondCol = CellReference.convertNumToColString(colnum) + (rownum+1);
		//=SUM(G92:OFFSET(G95,-1,0))
		String formula = "SUM("+firstCol+":OFFSET("+secondCol+",-1,0))";
		System.out.println(formula);
		return formula;
	}
	
	private String getAbstractAmountFormula(AbstractRanges range, int rownum) {
		String firstCell = CellReference.convertNumToColString(range.getQtyCol()) + (rownum+1);
		String secondCell = CellReference.convertNumToColString(range.getFullRateCol()) + (rownum+1);
		return "ROUND("+firstCell+"*"+secondCell+",0)";
	}
	
}
