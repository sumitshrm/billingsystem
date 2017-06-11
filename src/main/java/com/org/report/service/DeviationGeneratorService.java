package com.org.report.service;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.org.constants.Worksheets;
import com.org.entity.Item;
import com.org.entity.ItemAbstract;
import com.org.entity.MeasurementSheet;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.DeviationRanges;
import com.org.excel.util.MasterDataCellName;
import com.org.excel.util.XLColumnRange;
import com.org.util.Clause;

@Service
public class DeviationGeneratorService implements IExcelReportService{

	public void generateMasterData(MeasurementSheet msheet, XSSFWorkbook wb) throws Exception {
		System.out.println("writing deviation sheet master data");
		XSSFSheet xsheet = wb.getSheet(Worksheets.DEVIATIONSHEET);
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.AGGREEMENT_NO, msheet.getAggreement().getAggreementNum());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.NAME_OF_WORK, msheet.getAggreement().getNameOfWork());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.AGENCY, msheet.getAggreement().getAgency());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_START, msheet.getAggreement().getDateOfStart());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_COMPLETION_S, msheet.getAggreement().getDateOfCompletionS());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.SNO_OF_BILL, msheet.getSerialNumberDisplayFormat());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.CLAUSE, msheet.getAggreement().getClausePercentage() + "% " + msheet.getAggreement().getClause());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.TENDER_COST, msheet.getAggreement().getTenderCost());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.ESTIMATED_COST, msheet.getAggreement().getEstimatedCost()); 
		//Update CA title
		XLColumnRange range = new XLColumnRange(xsheet.getWorkbook(), DeviationRanges.STR_LESS_CA);
		XSSFCell cell = range.fetchSingleCell();
		ExcelUtill.writeCellValue(getCaTitle(msheet.getAggreement().getClausePercentage(), msheet.getAggreement().getClause()), cell);
	}
	
	public void removeReportData(MeasurementSheet msheet, XSSFWorkbook wb){
		XSSFSheet xsheet = wb.getSheet(Worksheets.DEVIATIONSHEET);
		DeviationRanges deviationRange = new DeviationRanges(wb);
		ExcelUtill.deleteRow(xsheet, deviationRange.getrItemNum().getFirstRowNum()+1, xsheet.getLastRowNum());
	}
	
	public void generateReport(MeasurementSheet msheet, XSSFWorkbook wb) throws Exception{
		XSSFSheet xsheet = wb.getSheet(Worksheets.DEVIATIONSHEET);
		DeviationRanges deviationRange = new DeviationRanges(wb);
		//delete existing data first 
		removeReportData(msheet, wb);
		int currRow = deviationRange.getrItemNum().getLastRowNum()+1;
		int startRow = currRow+1;
		int slNoCol = deviationRange.getrSlno().getFirstColNum();
		int itemNumCol = deviationRange.getrItemNum().getFirstColNum();
		int descCol = deviationRange.getrDescription().getFirstColNum();
		int unitCol = deviationRange.getrUnit().getFirstColNum();
		int qtyPerAgmtCol = deviationRange.getrQtyApAgmt().getFirstColNum();
		int qtyExecUptoDateCol = deviationRange.getrQtyEud().getFirstColNum();
		int devQtyCol = deviationRange.getrDevQty().getFirstColNum();
		int rateCol = deviationRange.getrRate().getFirstColNum();
		int rateLessCaCol = deviationRange.getrLessCa().getFirstColNum();
		int rateProposedCol = deviationRange.getrRateProposed().getFirstColNum();
		int devPercentCol = deviationRange.getrDevPercent().getFirstColNum();
		int devPlusCol = deviationRange.getrDevPlus().getFirstColNum();
		int devMinusCol = deviationRange.getrDevMinus().getFirstColNum();
		int totalDevCol = deviationRange.getrTotalDev().getFirstColNum();
		XSSFRow row  = null;
		Item item = null;
		XSSFCellStyle descriptionStyle = ExcelUtill.getBoxStyle(wb);
		descriptionStyle.setAlignment(HorizontalAlignment.JUSTIFY);
		XSSFCellStyle alignTopStyle = ExcelUtill.getBoxStyle(wb);
		alignTopStyle.setVerticalAlignment(VerticalAlignment.TOP);
		XSSFCellStyle boxStyle = ExcelUtill.getBoxStyle(wb);
		int slno = 1;
		for(ItemAbstract itemAbstract : msheet.getItemAbstractsSorted()){
			if(itemAbstract.getItem().isIsExtraItem() || itemAbstract.getTotalDeviation()==0){
				// do not write deviation report for extra items.
				continue; 
			}
			row = xsheet.createRow(currRow++);
			item = itemAbstract.getItem();
			ExcelUtill.writeCellValue(slno++, row.createCell(slNoCol), alignTopStyle);
			ExcelUtill.writeCellValue(item.getItemNumber(), row.createCell(itemNumCol), alignTopStyle);
			ExcelUtill.writeCellValue(item.getFullDescription(), row.createCell(descCol), descriptionStyle);
			ExcelUtill.writeCellValue(item.getUnit(), row.createCell(unitCol), boxStyle);
			ExcelUtill.writeCellValue(item.getQuantity(), row.createCell(qtyPerAgmtCol), boxStyle);
			ExcelUtill.writeCellValue("="+itemAbstract.getAbsCellRef(), row.createCell(qtyExecUptoDateCol), boxStyle);
			ExcelUtill.writeCellValue(getDeviationQtyFormula(currRow, qtyExecUptoDateCol, qtyPerAgmtCol), row.createCell(devQtyCol), boxStyle);
			ExcelUtill.writeCellValue(item.getFullRate(), row.createCell(rateCol), boxStyle);
			ExcelUtill.writeCellValue(getCaFormula(currRow, rateCol, item.getAggreement().getClausePercentage()), row.createCell(rateLessCaCol), boxStyle);
			ExcelUtill.writeCellValue(getRateProposedFormula(currRow, rateCol, rateLessCaCol, msheet.getAggreement().getClause()), row.createCell(rateProposedCol), boxStyle);
			ExcelUtill.writeCellValue(getDeviationPercentFormula(currRow, devQtyCol, qtyPerAgmtCol), row.createCell(devPercentCol), boxStyle);
			ExcelUtill.writeCellValue(getDeviationPlusFormula(currRow, devPercentCol, devQtyCol, rateProposedCol, item.getQuantityPerUnit()), row.createCell(devPlusCol), boxStyle);
			ExcelUtill.writeCellValue(getDeviationMinusFormula(currRow, devPercentCol, devQtyCol, rateProposedCol, item.getQuantityPerUnit()), row.createCell(devMinusCol), boxStyle);
			ExcelUtill.writeCellValue(getTotalDeviationFormula(currRow, devPlusCol, devMinusCol), row.createCell(totalDevCol), boxStyle);
		}
		int endRow = currRow;
		int devPercentRow1 = endRow+1;
		int devPercentRow2 = devPercentRow1+1;
		row = xsheet.createRow(currRow++); 
		//write the last row Total.
		boxStyle = ExcelUtill.getBoldBoxStyle(wb);
		ExcelUtill.writeCellValue(null, row.createCell(slNoCol), alignTopStyle);
		ExcelUtill.writeCellValue(null, row.createCell(itemNumCol), boxStyle);
		ExcelUtill.writeCellValue("Total", row.createCell(descCol), boxStyle);
		ExcelUtill.writeCellValue(null, row.createCell(qtyPerAgmtCol), boxStyle);
		ExcelUtill.writeCellValue(null, row.createCell(unitCol), boxStyle);
		ExcelUtill.writeCellValue(null, row.createCell(qtyPerAgmtCol), boxStyle);
		ExcelUtill.writeCellValue(null, row.createCell(qtyExecUptoDateCol), boxStyle);
		ExcelUtill.writeCellValue(null, row.createCell(devQtyCol), boxStyle);
		ExcelUtill.writeCellValue(null, row.createCell(rateCol), boxStyle);
		ExcelUtill.writeCellValue(null, row.createCell(rateLessCaCol), boxStyle);
		ExcelUtill.writeCellValue(null, row.createCell(rateProposedCol), boxStyle);
		ExcelUtill.writeCellValue(null, row.createCell(devPercentCol), boxStyle);
		ExcelUtill.writeCellValue(getDeviationPlusTotalFormula(startRow, endRow, devPlusCol),row.createCell(devPlusCol), boxStyle);
		ExcelUtill.writeCellValue(getDeviationMinusTotalFormula(startRow, endRow, devMinusCol),row.createCell(devMinusCol), boxStyle);
		ExcelUtill.writeCellValue(getTotalDeviationTotalFormula(startRow, endRow, totalDevCol),row.createCell(totalDevCol), boxStyle);
		//write percentage data.
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
		centerAlignedBottomLine.setBorderBottom(CellStyle.BORDER_THIN);
		
		row = xsheet.createRow(devPercentRow1);
		ExcelUtill.writeCellValue("Percentage of deviation =", row.createCell(descCol), rightAligned);
		ExcelUtill.writeCellValue(getCellRefferenceFormula(totalDevCol, endRow+1), row.createCell(unitCol), centerAlignedBottomLine);
		ExcelUtill.writeCellValue("x 100  =", row.createCell(qtyPerAgmtCol), centerAligned);
		
		row = xsheet.createRow(devPercentRow2);
		ExcelUtill.writeCellValue(msheet.getAggreement().getTenderCost(), row.createCell(unitCol), centerAligned);
		
		row = xsheet.getRow(devPercentRow1);
		//add 1 to devPercentRow1 & devPercentRow2 since rows are 0 indexed.
		ExcelUtill.writeCellValue(getTotalDeviationPercentageFormula(unitCol, devPercentRow1+1, devPercentRow2+1), row.createCell(qtyExecUptoDateCol), leftAlignedBold);
		
		//Write deviation summary
		ExcelUtill.writeCellValue(getCellRefferenceFormula(devPlusCol, endRow+1), deviationRange.getrSumaryDevPlus().fetchSingleCell());
		ExcelUtill.writeCellValue(getCellRefferenceFormula(devMinusCol, endRow+1), deviationRange.getrSumaryDevMinus().fetchSingleCell());
		ExcelUtill.writeCellValue(getCellRefferenceFormula(totalDevCol, endRow+1), deviationRange.getrSumaryTotalDev().fetchSingleCell());
		ExcelUtill.writeCellValue(getCellRefferenceFormula(qtyExecUptoDateCol, devPercentRow1+1), deviationRange.getrSumaryDevPercent().fetchSingleCell());
		boxStyle =  deviationRange.getrSumaryDevPercent().fetchSingleCell().getCellStyle();
		ExcelUtill.setPercentFormat(wb, boxStyle);
	}
	
	private String getDeviationQtyFormula(int row, int qtyExecUptoDateCol, int qtyPerAgmtCol){
		String f1 = ExcelUtill.intToChar(qtyExecUptoDateCol) + "" + (row);
		String f2 = ExcelUtill.intToChar(qtyPerAgmtCol) + "" + (row);
		return "=("+f1+"-"+f2+")";
	}
	
	private String getCaFormula(int row, int rateCol, float clausePercent ){
		//=ROUND(G17*45.95%,2)
		String formula = "";
		String f1 = ExcelUtill.intToChar(rateCol) + "" + (row);
		if(clausePercent>0){
			formula = "=ROUND("+f1+"*"+clausePercent+"%,2)";
		}
		return formula;
	}
	
	private String getRateProposedFormula(int row, int rateCol, int rateLessCaCol, Clause clause){
		String f1 = ExcelUtill.intToChar(rateCol) + "" + (row);
		String f2 = ExcelUtill.intToChar(rateLessCaCol) + "" + (row);
		String f3 = clause==Clause.Above?"+":"-";
		return "="+f1+f3+f2;
	}
	
	private String getCaTitle(float percent, Clause clause){
		String f1 = "CA @ " + percent +"% "+ clause.toString();
		return f1;
	}
	
	private String getDeviationPercentFormula(int row, int devQtyCol, int qtyAsPerAgmtCol){
		//=ROUND(F17/D17*100,2)
		String f1 = ExcelUtill.intToChar(devQtyCol) + "" + (row);
		String f2 = ExcelUtill.intToChar(qtyAsPerAgmtCol) + "" + (row);
		return "=ROUND("+f1+"/"+f2+"*100,2)";
	}
	
	private String getDeviationPlusFormula(int row, int devPercentCol, int devQtyCol, int rateProposedCol, double quantityPerUnit){
		//=ABS(ROUND(IF(J17>10,(F17*I17),0),0))
		String f1 = ExcelUtill.intToChar(devPercentCol) + "" + (row);
		String f2 = ExcelUtill.intToChar(devQtyCol) + "" + (row);
		String f3 = ExcelUtill.intToChar(rateProposedCol) + "" + (row);
		return "=ABS(ROUND(IF("+f1+">10,(("+f2+"*"+f3+")/"+quantityPerUnit+"),0),0))";
	}
	
	private String getDeviationMinusFormula(int row, int devPercentCol, int devQtyCol, int rateProposedCol, double quantityPerUnit){
		//=ABS(ROUND(IF(J17<-10,(F17*I17),0),0))
		String f1 = ExcelUtill.intToChar(devPercentCol) + "" + (row);
		String f2 = ExcelUtill.intToChar(devQtyCol) + "" + (row);
		String f3 = ExcelUtill.intToChar(rateProposedCol) + "" + (row);
		return "=ABS(ROUND(IF("+f1+"<-10,(("+f2+"*"+f3+")/"+quantityPerUnit+"),0),0))";
	}
	
	private String getTotalDeviationFormula(int row, int devPlusCol, int devMinusCol){
		String f1 = ExcelUtill.intToChar(devPlusCol) + "" + (row);
		String f2 = ExcelUtill.intToChar(devMinusCol) + "" + (row);
		return "="+f1+"+"+f2;
	}
	
	private String getDeviationPlusTotalFormula(int startRow, int endRow, int devPlusCol){
		//=SUM(K17:K25)
		String f1 = ExcelUtill.intToChar(devPlusCol) + "" + (startRow);
		String f2 = ExcelUtill.intToChar(devPlusCol) + "" + (endRow);
		return "=SUM("+f1+":"+f2+")";
	}
	
	private String getDeviationMinusTotalFormula(int startRow, int endRow, int devMinusCol){
		//=SUM(K17:K25)
		String f1 = ExcelUtill.intToChar(devMinusCol) + "" + (startRow);
		String f2 = ExcelUtill.intToChar(devMinusCol) + "" + (endRow);
		return "=SUM("+f1+":"+f2+")";
	}
	
	private String getTotalDeviationTotalFormula(int startRow, int endRow, int devTotalCol){
		//=SUM(K17:K25)
		String f1 = ExcelUtill.intToChar(devTotalCol) + "" + (startRow);
		String f2 = ExcelUtill.intToChar(devTotalCol) + "" + (endRow);
		return "=SUM("+f1+":"+f2+")";
	}
	
	private String getTotalDeviationPercentageFormula(int colNum, int uperRow, int lowerRow){
		//=C27/C28*100
		String f1 = ExcelUtill.intToChar(colNum) + "" + (uperRow);
		String f2 = ExcelUtill.intToChar(colNum) + "" + (lowerRow);
		return "="+f1+"/"+f2+"*100";
	}
	
	private String getCellRefferenceFormula(int colNum,int rowNum){
		String f1 = ExcelUtill.intToChar(colNum) + "" + (rowNum);
		return "="+f1;
	}
}
