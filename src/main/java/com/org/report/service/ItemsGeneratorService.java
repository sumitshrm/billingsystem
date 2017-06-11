package com.org.report.service;

import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.springframework.stereotype.Service;

import com.org.constants.Worksheets;
import com.org.entity.Item;
import com.org.entity.MeasurementSheet;
import com.org.excel.gateway.ResponseStatus;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.ExtraItemRanges;
import com.org.excel.util.ItemRanges;
import com.org.excel.util.XLColumnRange;

@Service
public class ItemsGeneratorService {
	
	public void writeItems(Set<Item> items, XSSFWorkbook wb, MeasurementSheet msheet, boolean writeExtraItem) throws Exception {
		
		XSSFSheet xsheet = wb.getSheet(Worksheets.ITEMS_SHEET);
		XSSFSheet xsheet_extra_items = wb
				.getSheet(Worksheets.EXTRA_ITEMS_SHEET);
		XSSFRow currentRow = null;
		int itemStartRow = 1;
		int extraItemStartRow = 1;
		ItemRanges itemRange = new ItemRanges(wb, false);
		ItemRanges extraItemRange = new ItemRanges(wb, true);
		ItemRanges range = null;
		XSSFCellStyle boxstyle =  ExcelUtill.getBoxStyle(wb);
		XSSFCellStyle style_align_justify =  ExcelUtill.getBoxStyle(wb);
		XSSFCellStyle style_align_top =  ExcelUtill.getBoxStyle(wb); 
		style_align_justify.setAlignment(XSSFCellStyle.ALIGN_JUSTIFY);
		style_align_top.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
		boolean existingExtraItemsDeleted = false;
		for (Item item : items) {
			if(item.isIsExtraItem()){
				if(!writeExtraItem){
					continue;
				}
				range = extraItemRange;
			}else{
				range = itemRange;
			}
			if (msheet != null && item.getMeasurementSheetId()!=null
					&& item.getMeasurementSheetId().compareTo(msheet.getId())==0) {
				if(!existingExtraItemsDeleted){
					// to be called only once.
					existingExtraItemsDeleted = true;
					ExcelUtill.deleteRow(xsheet_extra_items, extraItemStartRow, xsheet_extra_items.getLastRowNum());
				}
				currentRow = xsheet_extra_items.createRow(extraItemStartRow++);

			}else{
				currentRow = xsheet.createRow(itemStartRow++);
			}
			ExcelUtill.writeCellValue(item.getQuantity(),
					currentRow.createCell(range.getQtyCol()), boxstyle);
			ExcelUtill.writeCellValue(item.getQuantityPerUnit(),
					currentRow.createCell(range.getQtyPerUnitCol()), boxstyle);
			if (item.isIsExtraItem()) {
				ExcelUtill.writeCellValue(item.getDsrRate(),
						currentRow.createCell(range.getDsrRateCol()), boxstyle);
			}
			ExcelUtill.writeCellValue(item.getFullRate(),
					currentRow.createCell(range.getFullRateCol()), boxstyle);
			ExcelUtill.writeCellValue(item.getPartRate(),
					currentRow.createCell(range.getPartRateCol()), boxstyle);
			ExcelUtill.writeCellValue(item.getDrsCode(),
					currentRow.createCell(range.getDsrCodeCol()), boxstyle);
			ExcelUtill.writeCellValue(item.getItemNumber(),
					currentRow.createCell(range.getItemNumCol()), style_align_top);
			ExcelUtill.writeCellValue(item.getDescription(),
					currentRow.createCell(range.getDescCol()), style_align_justify);
			ExcelUtill.writeCellValue(item.getUnit(),
					currentRow.createCell(range.getUnitCol()), boxstyle);

		}

	}
	
	public void removeExtraItemsData(MeasurementSheet msheet){
		XSSFSheet xsheet_extra_items = msheet.getDocument().getWorkbook()
				.getSheet(Worksheets.EXTRA_ITEMS_SHEET);
		ExcelUtill.deleteRow(xsheet_extra_items, 1, xsheet_extra_items.getLastRowNum());
	}
	
	public void writeExistingItemsDataInConfig(Set<Item> items, XSSFWorkbook wb) throws Exception{
		StringBuilder items_str = new StringBuilder();
		String separator = "";
		for(Item item : items){
			items_str.append(separator + item.getItemNumber());
			separator =",";
		}
		XLColumnRange range = new XLColumnRange(wb, ItemRanges.T_METADATA_EXISTING_ITEMS_IN_DATABASE);
		ExcelUtill.writeCellValue(items_str, range.fetchSingleCell());
	}

}
