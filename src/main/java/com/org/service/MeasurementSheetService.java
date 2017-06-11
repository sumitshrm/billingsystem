package com.org.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.org.constants.AbstractSheetConstants;
import com.org.constants.Worksheets;
import com.org.entity.Aggreement;
import com.org.entity.Document;
import com.org.entity.Item;
import com.org.entity.ItemAbstract;
import com.org.entity.MeasurementSheet;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.MasterDataCellName;
import com.org.excel.util.XLColumnRange;
import com.org.report.service.AbstractGeneratorService;
import com.org.report.service.ScheduleGeneratorService;

@Service
public class MeasurementSheetService {
	
	@Autowired
	private DocumentService documentService;

	@Autowired
	@Qualifier(value="abstractGeneratorService")
	private AbstractGeneratorService abstractService;
	
	@Autowired
	private ScheduleGeneratorService scheduleService;
	
	public void updateItems(MeasurementSheet msheet){
		XSSFWorkbook wb = msheet.getDocument().getWorkbook();
		XSSFSheet xsheet_items = wb.getSheet(Worksheets.ITEMS_SHEET);
		XSSFSheet xsheet_extraitems = wb.getSheet(Worksheets.EXTRA_ITEMS_SHEET);
		XSSFSheet xsheet_abstract = wb.getSheet(Worksheets.ABSTRACTSHEET);
		createNewItems(msheet, xsheet_items, false);
		createNewItems(msheet, xsheet_extraitems, true);
		updatePartRateFromAbstract(msheet, xsheet_abstract);
		
	}
 
	private void createNewItems(MeasurementSheet msheet, XSSFSheet xsheet,
			boolean isExtraItem) {
		Iterator<Row> rowIterator = xsheet.iterator();
		rowIterator.next();
	    while(rowIterator.hasNext()) {
	    	
	        Row row = rowIterator.next();
	        Cell c_itemnumber = row.getCell(1);
	        Cell c_sub_itemnumber = row.getCell(2);
	        if(c_itemnumber!=null && !"".equals(c_itemnumber.toString()))
	        {
		        String itemnumber = ExcelUtill.getCellValueAsString(c_itemnumber);
		        Item extraItem = msheet.getAggreement().getItemByItemNumber(itemnumber);
		        if(extraItem == null){
		        	extraItem = new Item();
		        }  
		        int colnum = 1; 
		        extraItem.setAggreement(msheet.getAggreement());
	        	extraItem.setItemNumber(ExcelUtill.getCellValueAsString(row.getCell(colnum++)));
	        	//extraItem.setSubItemNumber(ExcelUtill.getCellValueAsString(row.getCell(colnum++)));
	        	extraItem.setDescription(ExcelUtill.getCellValueAsString(row.getCell(colnum++)));
	        	Cell numcell = row.getCell(colnum++);
	        	//QUANTITY
	        	if(numcell!=null && numcell.getCellType()!=Cell.CELL_TYPE_BLANK && 
	        			numcell.getCellType()==Cell.CELL_TYPE_NUMERIC){
	    	    		extraItem.setQuantity(numcell.getNumericCellValue());
	    	    }
	        	
	        	numcell = row.getCell(colnum++);
	        	if(numcell!=null && numcell.getCellType()!=Cell.CELL_TYPE_BLANK && 
	        			numcell.getCellType()==Cell.CELL_TYPE_NUMERIC){
	    	    		extraItem.setQuantityPerUnit(numcell.getNumericCellValue());
	    	    }
	        	extraItem.setUnit(ExcelUtill.getCellValueAsString(row.getCell(colnum++)));
	        	
	        	numcell = row.getCell(colnum++);
	        	if(numcell!=null && numcell.getCellType()!=Cell.CELL_TYPE_BLANK && 
	        			numcell.getCellType()==Cell.CELL_TYPE_NUMERIC){
	        		extraItem.setFullRate(numcell.getNumericCellValue());
	        	} 
	        	numcell = row.getCell(colnum++);
	        	if(numcell!=null && numcell.getCellType()!=Cell.CELL_TYPE_BLANK && 
	        			numcell.getCellType()==Cell.CELL_TYPE_NUMERIC){
	    	    		extraItem.setPartRate(numcell.getNumericCellValue());
	    	    }	  
	        	
	        	extraItem.setIsExtraItem(isExtraItem);
	        	extraItem.persist();
	        }
	    }
	}
	
	public void updatePartRateFromAbstract(MeasurementSheet msheet, XSSFSheet xsheet){
		XLColumnRange partRateRange = new XLColumnRange(xsheet.getWorkbook(), AbstractSheetConstants.RANGE_A_METADATA_PART_RATE_UPDATED);
		XLColumnRange abstractTitles = new XLColumnRange(xsheet.getWorkbook(), AbstractSheetConstants.RANGE_ABSTRACT_TITLES);
		int startRow = abstractTitles.getFirstRowNum();
		System.out.println("start row : " + startRow);
		int currentRow = startRow;
		int endRow = xsheet.getLastRowNum();
		System.out.println("end row:" + endRow);
		int colnum = partRateRange.getFirstColNum();
		System.out.println("col num:" + colnum);
		Cell cell;
		String cellValue;	
		String fullItemNum;
		double partRate;
		Item item;
		while(currentRow <= endRow){
			cell = xsheet.getRow(currentRow).getCell(colnum);
			if(cell!=null && cell.getCellType()!=Cell.CELL_TYPE_BLANK && 
					!ExcelUtill.getCellValueAsString(cell).equals("")){
				cellValue = ExcelUtill.getCellValueAsString(cell);
				fullItemNum = cellValue.split(",")[0];
				partRate = Double.parseDouble(cellValue.split(",")[1]);
				item = msheet.getAggreement().getItemByItemNumber(fullItemNum);
				item.setPartRate(partRate);
				item.persist();
			}
			
			currentRow++;
		}
	}
	
	public void uploadExcelDocument(MeasurementSheet measurementSheet,
			MultipartFile file) throws Exception {
		MeasurementSheet msheetMerged = MeasurementSheet
				.findMeasurementSheet(measurementSheet.getId());
		boolean currentIsFinalBill = msheetMerged.isIsFinalBill();
		boolean selectedIsFinalBill = measurementSheet.isIsFinalBill();

		if (!file.isEmpty()) {
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(new File(measurementSheet
							.getDocument().getUrl())));
			FileCopyUtils.copy(file.getInputStream(), stream);
			stream.close();
			System.out.println("File uploaded successfully");
		}

		if (selectedIsFinalBill != currentIsFinalBill) {
			XSSFWorkbook wb = msheetMerged.getDocument().getWorkbook();
			ExcelUtill.writeMasterData(wb.getSheet(Worksheets.ABSTRACTSHEET),
					MasterDataCellName.SNO_OF_BILL,
					msheetMerged.getSerialNumberDisplayFormat());
			ExcelUtill.writeMasterData(
					wb.getSheet(Worksheets.MEASUREMENTSHEET),
					MasterDataCellName.SNO_OF_BILL,
					msheetMerged.getSerialNumberDisplayFormat());
			ExcelUtill.writeMasterData(wb.getSheet(Worksheets.DEVIATIONSHEET),
					MasterDataCellName.SNO_OF_BILL,
					msheetMerged.getSerialNumberDisplayFormat());
			ExcelUtill.writeMasterData(wb.getSheet(Worksheets.FNFB_SCHEDULE),
					MasterDataCellName.SNO_OF_BILL,
					msheetMerged.getSerialNumberDisplayFormat());
			abstractService.prepareAbstractSheet(msheetMerged, wb,
					wb.getSheet(Worksheets.ABSTRACTSHEET));
			scheduleService.generateMasterData(msheetMerged, wb);
			documentService.removeReportData(msheetMerged);
			List<ItemAbstract> itemAbstracts = ItemAbstract
					.findItemAbstractsByMeasurementSheet(measurementSheet)
					.getResultList();
			for (ItemAbstract itemAbstract : itemAbstracts) {
				itemAbstract.setTotal(0);
				itemAbstract.persist();
			}
			msheetMerged.getDocument().save();
		}
		msheetMerged.setTitle(measurementSheet.getTitle());
		msheetMerged.setIsFinalBill(measurementSheet.isIsFinalBill());
		msheetMerged.setSerialNumber(measurementSheet.getSerialNumber());
		msheetMerged.persist();

	}
	
}
