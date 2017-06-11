package com.org.report.service;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.org.constants.MeasurementSheetConstants;
import com.org.constants.Worksheets;
import com.org.entity.ItemAbstract;
import com.org.entity.ItemAbstractDataTo;
import com.org.entity.MeasurementSheet;
import com.org.excel.service.ExcelUtill;

@Service(value="abstractGeneratorServiceV2")
public class AbstractGeneratorServiceV2 extends AbstractGeneratorService{
		
	final static Logger logger = Logger.getLogger(AbstractGeneratorServiceV2.class);
	
		
	@Override
	public void generateReport(MeasurementSheet msheet, XSSFWorkbook workbook) throws Exception{
		// create the map of items and extra items
		removeReportData(msheet, workbook);
		prepareItemAbstractData(msheet);
		loadItemAbstractDataFromWorkbook(workbook, msheet);
		writeAbstractToWorkbook(msheet, workbook);
		updateConfigData(msheet, workbook);
		msheet.persist();

	}
	
	private void updateConfigData(MeasurementSheet msheet, XSSFWorkbook workbook) throws Exception {
		XSSFRow row;
		XSSFCell cell;
		int data_itemnum_column = ExcelUtill.getColumnNumberByRangeName(workbook, MeasurementSheetConstants.CD_ABSTRACT);
		for(ItemAbstract itemAbstract : msheet.getItemAbstracts()){
			for(ItemAbstractDataTo dataTo : itemAbstract.getItemDataTos()){
				row = dataTo.getConfigDataRow();
				cell = row.createCell(data_itemnum_column);
				//cell.setCellFormula("="+dataTo.getMeasCellRef());
				ExcelUtill.writeCellValue("="+Worksheets.ABSTRACTSHEET+"!"+dataTo.getAbsCellRef(), cell);
			}
		}

	}

	@Override
	protected void loadItemAbstractDataFromWorkbook(XSSFWorkbook workbook, MeasurementSheet msheet) {
		XSSFSheet config_data_sheet = workbook.getSheet(Worksheets.CONFIG_DATA_SHEET);
		Iterator<Row> rowIterator = config_data_sheet.iterator();
		ItemAbstract itemAbs = null;
		int data_itemnum_column = ExcelUtill.getColumnNumberByRangeName(workbook, MeasurementSheetConstants.CD_ITEM_NUM);
		int data_total_column = ExcelUtill.getColumnNumberByRangeName(workbook, MeasurementSheetConstants.CD_TOTAL);
		while(rowIterator.hasNext()){
			XSSFRow row = (XSSFRow)rowIterator.next();
			String itemNum = ExcelUtill.getCellValueAsString(row.getCell(data_itemnum_column));
			itemAbs = msheet.getItemAbstractByItemNum(itemNum);
			Cell totalCell = row.getCell(data_total_column);
			if(itemAbs!=null){
				ItemAbstractDataTo absData = new ItemAbstractDataTo();
				absData.setTotal(totalCell.getNumericCellValue());
				absData.setMeasCellRef(totalCell.getCellFormula().replace(Worksheets.MEASUREMENTSHEET+"!", ""));
				absData.setConfigDataRow(row);
				itemAbs.getItemDataTos().add(absData);
			}
		}
		
	}
	
	@Override
	protected void setRefValueInMeasurementSheet(XSSFWorkbook workbook,
			String source, Cell target) {
		//DO nothing in version 2
	}
	
	@Override
	protected void setHyperlinkInMeasurementSheet(XSSFWorkbook workbook, String source, Cell target, ItemAbstractDataTo itemAbsData) {
		XSSFSheet msheet = workbook.getSheet(Worksheets.MEASUREMENTSHEET);
		Cell c_description = null;
		CellReference ref_source = new CellReference(source);
		 Row r = msheet.getRow(ref_source.getRow());
		 if (r != null) {
		    c_description = r.getCell(r.getFirstCellNum());
		 }
		 Cell configDataPageNumCell = itemAbsData.getConfigDataRow().getCell(ExcelUtill.getColumnNumberByRangeName(workbook, MeasurementSheetConstants.CD_A_PAGE));
		 if(configDataPageNumCell==null){
			 configDataPageNumCell =  itemAbsData.getConfigDataRow().createCell(ExcelUtill.getColumnNumberByRangeName(workbook, MeasurementSheetConstants.CD_A_PAGE));
		 }
		 c_description.setCellFormula("HYPERLINK(CELL(\"address\","+Worksheets.ABSTRACTSHEET+"!"+ExcelUtill.getCellReference(target)+"), T_PAGE_NUMBER & "+Worksheets.CONFIG_DATA_SHEET+"!"+ExcelUtill.getCellReference(configDataPageNumCell)+")");
		
	}

}
