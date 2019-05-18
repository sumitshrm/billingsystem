package com.org.report.service;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.bytecode.buildtime.ExecutionException;
import org.springframework.stereotype.Service;

import com.org.constants.Worksheets;
import com.org.entity.Item;
import com.org.entity.ItemAbstract;
import com.org.entity.MeasurementSheet;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.ExtraItemStatementRanges;
import com.org.excel.util.MasterDataCellName;
import com.org.excel.util.XLColumnRange;
import com.org.util.Clause;
import com.org.web.MeasurementSheetController;

@Service
public class ExtraItemStatementGeneratorService implements IExcelReportService {
	
	final static Logger logger = Logger.getLogger(ExtraItemStatementGeneratorService.class);


	public void generateMasterData(MeasurementSheet msheet, XSSFWorkbook wb)
			throws Exception {
		System.out.println("writing extra item statement master data");
		XSSFSheet xsheet = wb.getSheet(Worksheets.EXTRA_ITEM_STATEMENT_SHEET);
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.AGGREEMENT_NO,
				msheet.getAggreement().getAggreementNum());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.NAME_OF_WORK,
				msheet.getAggreement().getNameOfWork());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.AGENCY, msheet
				.getAggreement().getAgency());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_START,
				msheet.getAggreement().getDateOfStart());
		ExcelUtill.writeMasterData(xsheet,
				MasterDataCellName.DATE_OF_COMPLETION_S, msheet.getAggreement()
						.getDateOfCompletionS());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.SNO_OF_BILL,
				msheet.getSerialNumberDisplayFormat());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.CLAUSE, msheet
				.getAggreement().getClausePercentage()
				+ "% "
				+ msheet.getAggreement().getClause());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.TENDER_COST,
				msheet.getAggreement().getTenderCost());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.ESTIMATED_COST,
				msheet.getAggreement().getEstimatedCost());
		// Update CA title
		XLColumnRange range = new XLColumnRange(xsheet.getWorkbook(),
				ExtraItemStatementRanges.STR_LESS_CA);
		XSSFCell cell = range.fetchSingleCell();
		ExcelUtill.writeCellValue(
				getCaTitle(msheet.getAggreement().getClausePercentage(), msheet
						.getAggreement().getClause()), cell);
	}

	public void removeReportData(MeasurementSheet msheet, XSSFWorkbook wb,
			ExtraItemStatementRanges extraItemsRanges) {
		XSSFSheet xsheet = wb.getSheet(Worksheets.EXTRA_ITEM_STATEMENT_SHEET);
		ExcelUtill.deleteRow(xsheet, extraItemsRanges.getrItemNum()
				.getFirstRowNum() + 2, xsheet.getLastRowNum());
	}

	public void generateReport(MeasurementSheet msheet, XSSFWorkbook wb) throws Exception {
		try {

			System.out.println("writing extra item statement report");
			XSSFSheet xsheet = wb.getSheet(Worksheets.EXTRA_ITEM_STATEMENT_SHEET);
			// delete existing data first
			ExtraItemStatementRanges extraItemRange = new ExtraItemStatementRanges(wb);
			removeReportData(msheet, wb, extraItemRange);
			int currRow = extraItemRange.getrItemNum().getLastRowNum() + 2;
			int startRow = currRow + 1;
			int slNoCol = extraItemRange.getrSlno().getFirstColNum();
			int itemNumCol = extraItemRange.getrItemNum().getFirstColNum();
			int dsrRateCol = extraItemRange.getrDsrCode().getFirstColNum();
			int descCol = extraItemRange.getrDescription().getFirstColNum();
			int unitCol = extraItemRange.getrUnit().getFirstColNum();
			int qtyExecUptoDateCol = extraItemRange.getrQtyEud().getFirstColNum();
			int rateCol = extraItemRange.getrRate().getFirstColNum();
			int lessCaCol = extraItemRange.getrLessCa().getFirstColNum();
			int rateProposedCol = extraItemRange.getrRateProposed().getFirstColNum();
			int totalAmountCol = extraItemRange.getrTotalAmount().getFirstColNum();
			int remarksCol = extraItemRange.getrRemarks().getFirstColNum();
			XSSFRow row = null;
			XSSFCellStyle descriptionStyle = ExcelUtill.getBoxStyle(wb);
			descriptionStyle.setAlignment(HorizontalAlignment.JUSTIFY);
			XSSFCellStyle alignTopStyle = ExcelUtill.getBoxStyle(wb);
			alignTopStyle.setVerticalAlignment(VerticalAlignment.TOP);
			XSSFCellStyle boxStyle = ExcelUtill.getBoxStyle(wb);
			int slno = 1;
			Map<String, ItemAbstract> itemAbstractMap = msheet.getItemAbstractsMap();
			boolean hasExtraItem = false;
			for (Item item : msheet.getAggreement().getItems()) {
				try {

					/*
					 * if(itemAbstract.getItem().isIsExtraItem() ||
					 * itemAbstract.getTotalDeviation()==0){ // do not write deviation report for
					 * extra items. continue; }
					 */
					if (!item.isIsExtraItem()) {
						// write statement for extra items only
						continue;
					}
					hasExtraItem=true;
					row = xsheet.createRow(currRow++);
					if (item.isValidItem()) {
						ExcelUtill.writeCellValue(slno++, row.createCell(slNoCol), alignTopStyle);
						ExcelUtill.writeCellValue(item.getItemNumber(), row.createCell(itemNumCol), alignTopStyle);
						ExcelUtill.writeCellValue(item.getDrsCode(), row.createCell(dsrRateCol), alignTopStyle);
						ExcelUtill.writeCellValue(item.getDescription(), row.createCell(descCol), descriptionStyle);
						ExcelUtill.writeCellValue(item.getUnit(), row.createCell(unitCol), boxStyle);
						ExcelUtill.writeCellValue("=" + itemAbstractMap.get(item.getItemNumber()).getAbsCellRef(),
								row.createCell(qtyExecUptoDateCol), boxStyle);
						ExcelUtill.writeCellValue(item.getDsrRate(), row.createCell(rateCol), boxStyle);
						ExcelUtill.writeCellValue(
								getCaFormula(currRow, rateCol, item.getAggreement().getClausePercentage()),
								row.createCell(lessCaCol), boxStyle);
						ExcelUtill.writeCellValue(
								getRateProposedFormula(currRow, rateCol, lessCaCol, msheet.getAggreement().getClause()),
								row.createCell(rateProposedCol), boxStyle);
						ExcelUtill.writeCellValue(getTotalAmountFormula(currRow, qtyExecUptoDateCol, rateProposedCol),
								row.createCell(totalAmountCol), boxStyle);
						ExcelUtill.writeCellValue(null, row.createCell(remarksCol), boxStyle);
					} else {
						ExcelUtill.writeCellValue(slno++, row.createCell(slNoCol), alignTopStyle);
						ExcelUtill.writeCellValue(item.getItemNumber(), row.createCell(itemNumCol), alignTopStyle);
						ExcelUtill.writeCellValue(item.getDrsCode(), row.createCell(dsrRateCol), alignTopStyle);
						ExcelUtill.writeCellValue(item.getDescription(), row.createCell(descCol), descriptionStyle);
						ExcelUtill.writeCellValue(null, row.createCell(unitCol), boxStyle);
						ExcelUtill.writeCellValue(null, row.createCell(qtyExecUptoDateCol), boxStyle);
						ExcelUtill.writeCellValue(null, row.createCell(rateCol), boxStyle);
						ExcelUtill.writeCellValue(null, row.createCell(lessCaCol), boxStyle);
						ExcelUtill.writeCellValue(null, row.createCell(rateProposedCol), boxStyle);
						ExcelUtill.writeCellValue(null, row.createCell(totalAmountCol), boxStyle);
						ExcelUtill.writeCellValue(null, row.createCell(remarksCol), boxStyle);
					}
				} catch (Exception e) {
					logger.error("Error writing extraitemstatement, item : " + item.getItemNumber() + "\r\n"+e.getMessage(), e);
					throw new ExecutionException("Error writing extraitemstatement, item : " + item.getItemNumber() + "\r\n"+e.getMessage(), e);
				}

				

			}
			
			if(hasExtraItem) {
				row = xsheet.createRow(currRow++);
				// write the last row Total.
				boxStyle = ExcelUtill.getBoldBoxStyle(wb);
				ExcelUtill.writeCellValue(null, row.createCell(slNoCol), alignTopStyle);
				ExcelUtill.writeCellValue(null, row.createCell(itemNumCol), boxStyle);
				ExcelUtill.writeCellValue(null, row.createCell(dsrRateCol), boxStyle);
				ExcelUtill.writeCellValue("Total", row.createCell(descCol), boxStyle);
				ExcelUtill.writeCellValue(null, row.createCell(unitCol), boxStyle);
				ExcelUtill.writeCellValue(null, row.createCell(qtyExecUptoDateCol), boxStyle);
				ExcelUtill.writeCellValue(null, row.createCell(rateCol), boxStyle);
				ExcelUtill.writeCellValue(null, row.createCell(lessCaCol), boxStyle);
				ExcelUtill.writeCellValue(null, row.createCell(rateProposedCol), boxStyle);
				ExcelUtill.writeCellValue(getFinalTotalAmountFormula(startRow, currRow - 1, totalAmountCol),
						row.createCell(totalAmountCol), boxStyle);
				ExcelUtill.writeCellValue(null, row.createCell(remarksCol), boxStyle);

				// write percentage data.
				int endRow = currRow;
				int devPercentRow1 = endRow;
				int devPercentRow2 = devPercentRow1 + 1;
				XSSFCellStyle leftAligned = wb.createCellStyle();
				leftAligned.setAlignment(HorizontalAlignment.LEFT);
				XSSFCellStyle centerAligned = wb.createCellStyle();
				centerAligned.setAlignment(HorizontalAlignment.CENTER);
				XSSFCellStyle rightAligned = wb.createCellStyle();
				rightAligned.setAlignment(HorizontalAlignment.RIGHT);
				XSSFCellStyle leftAlignedBold = wb.createCellStyle();
				leftAlignedBold.setAlignment(HorizontalAlignment.LEFT);
				ExcelUtill.setBoldFont(wb, leftAlignedBold);
				ExcelUtill.setPercentFormat(wb, leftAlignedBold);
				XSSFCellStyle centerAlignedBottomLine = wb.createCellStyle();
				centerAlignedBottomLine.setAlignment(HorizontalAlignment.CENTER);
				centerAlignedBottomLine.setBorderBottom(BorderStyle.THIN);

				row = xsheet.createRow(devPercentRow1);
				ExcelUtill.writeCellValue("Percentage of EIS = ", row.createCell(descCol), rightAligned);
				ExcelUtill.writeCellValue(getCellRefferenceFormula(totalAmountCol, endRow), row.createCell(unitCol),
						centerAlignedBottomLine);
				ExcelUtill.writeCellValue("x 100  =", row.createCell(qtyExecUptoDateCol), centerAligned);

				row = xsheet.createRow(devPercentRow2);
				ExcelUtill.writeCellValue(msheet.getAggreement().getTenderCost(), row.createCell(unitCol),
						centerAligned);

				row = xsheet.getRow(devPercentRow1);
				// add 1 to devPercentRow1 & devPercentRow2 since rows are 0 indexed.
				ExcelUtill.writeCellValue(getTotalPercentageFormula(unitCol, devPercentRow1 + 1, devPercentRow2 + 1),
						row.createCell(qtyExecUptoDateCol), leftAlignedBold);
			}
		} catch (Exception e) {
			logger.error("Error writing extraitemstatement \r\n"+e.getMessage(), e);
			throw new Exception("Error writing extraitemstatement \r\n"+e.getMessage(), e);
		}
	}

	private String getCaFormula(int row, int rateCol, float clausePercent) {
		// =ROUND(G17*45.95%,2)
		String formula = "";
		String f1 = ExcelUtill.intToChar(rateCol) + "" + (row);
		if (clausePercent > 0) {
			formula = "=ROUND(" + f1 + "*" + clausePercent + "%,2)";
		}else{
			formula = null;
		}
		return formula;
	}

	private String getRateProposedFormula(int row, int rateCol,
			int rateLessCaCol, Clause clause) {
		String f1 = ExcelUtill.intToChar(rateCol) + "" + (row);
		String f2 = ExcelUtill.intToChar(rateLessCaCol) + "" + (row);
		String f3 = clause == Clause.Above ? "+" : "-";
		return "=" + f1 + f3 + f2;
	}

	private String getCaTitle(float percent, Clause clause) {
		String f1 = "CA @ " + percent + "% " + clause.toString();
		return f1;
	}

	private String getTotalAmountFormula(int row, int qtyExecUptoDateCol,
			int rateCol) {
		String f1 = ExcelUtill.intToChar(qtyExecUptoDateCol) + "" + (row);
		String f2 = ExcelUtill.intToChar(rateCol) + "" + (row);
		return "=ABS((ROUND(" + f1 + "*" + f2 + ",0)))";
	}

	private String getFinalTotalAmountFormula(int firstRow, int lastRow,
			int totalAmountCol) {
		String f1 = ExcelUtill.intToChar(totalAmountCol) + "" + (firstRow);
		String f2 = ExcelUtill.intToChar(totalAmountCol) + "" + (lastRow);
		return "=SUM(" + f1 + ":" + f2 + ")";
	}
	
	private String getCellRefferenceFormula(int colNum,int rowNum){
		String f1 = ExcelUtill.intToChar(colNum) + "" + (rowNum);
		return "="+f1;
	}
	
	private String getTotalPercentageFormula(int colNum, int uperRow, int lowerRow){
		//=C27/C28*100
		String f1 = ExcelUtill.intToChar(colNum) + "" + (uperRow);
		String f2 = ExcelUtill.intToChar(colNum) + "" + (lowerRow);
		return "="+f1+"/"+f2+"*100";
	}

}
