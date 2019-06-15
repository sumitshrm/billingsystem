package com.org.report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.org.constants.AbstractSheetConstants;
import com.org.constants.Worksheets;
import com.org.entity.Item;
import com.org.entity.ItemAbstract;
import com.org.entity.MeasurementSheet;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.MasterDataCellName;
import com.org.excel.util.RunningBillScheduleRange;
import com.org.excel.util.ScheduleRange;
import com.org.exception.ExcelParseException;
import com.org.util.Clause;

@Service
public class ScheduleGeneratorService implements IExcelReportService{
	
	final static Logger logger = Logger.getLogger(ScheduleGeneratorService.class);

	
	public void generateMasterData(MeasurementSheet msheet, XSSFWorkbook wb) throws Exception{
		XSSFSheet xsheet = prepareScheduleSheet(msheet, wb);
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.AGGREEMENT_NO, msheet.getAggreement().getAggreementNum());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.NAME_OF_WORK, msheet.getAggreement().getNameOfWork());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.AGENCY, msheet.getAggreement().getAgency());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_START, msheet.getAggreement().getDateOfStart());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_COMPLETION_S, msheet.getAggreement().getDateOfCompletionS());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_COMPLETION_A, msheet.getAggreement().getDateOfCompletionA());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.SNO_OF_BILL, msheet.getSerialNumberDisplayFormat());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.CLAUSE, msheet.getAggreement().getClausePercentage() + "% " + msheet.getAggreement().getClause());
		//ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_ABSTRACT, msheet.getAggreement().getDateOfAbstract());
	}
	
	public void removeReportData(MeasurementSheet msheet, XSSFWorkbook wb){
		XSSFSheet xsheet = getScheduleSheet(msheet, wb);
		ScheduleRange scheduleRange = getScheduleRange(msheet, wb);
		//delete existing data first 
		ExcelUtill.deleteRow(xsheet, scheduleRange.getrItemNum().getFirstRowNum()+1, xsheet.getLastRowNum());
	}
	
	public void generateReport(MeasurementSheet msheet, XSSFWorkbook wb) throws Exception{
		try {

			XSSFSheet xsheet = getScheduleSheet(msheet, wb);
			ScheduleRange scheduleRange = getScheduleRange(msheet, wb);
			//delete existing data first 
			removeReportData(msheet, wb);
			int currentRow = scheduleRange.getrItemNum().getFirstRowNum()+1;
			int firstRow = currentRow;
			int itemNumCol = scheduleRange.getrItemNum().getFirstColNum();
			int itemDescCol = scheduleRange.getrDescriptionOfItem().getFirstColNum();
			int quantityCol = scheduleRange.getrQuantExecUptoDate().getFirstColNum();
			int unitCol = scheduleRange.getrUnit().getFirstColNum();
			int amountCol = scheduleRange.getrTotalAmount().getFirstColNum();
			XSSFCellStyle descriptionStyle = ExcelUtill.getBoxStyle(wb);
			descriptionStyle.setAlignment(HorizontalAlignment.JUSTIFY);
			XSSFCellStyle alignTopStyle = ExcelUtill.getBoxStyle(wb);
			alignTopStyle.setVerticalAlignment(VerticalAlignment.TOP);
			XSSFCellStyle boxStyle = ExcelUtill.getBoxStyle(wb);
			XSSFCellStyle boldStyle = ExcelUtill.getBoldFont(wb);
			List<Item> extraItems = new ArrayList<Item>();
			List<ItemAbstract> extraItemAbstract = new ArrayList<ItemAbstract>();
			Map<String, ItemAbstract> itemAbstractMap = msheet.getItemAbstractsMap();
			//Write itemabstract data only for items (exclude extra items)
			for(Item item : msheet.getAggreement().getItems()){
				try {

					if(!item.isIsExtraItem()){
						if(item.isValidItem()){
							currentRow = writeScheduleData(msheet, xsheet, scheduleRange,
									currentRow, itemNumCol, itemDescCol, quantityCol, unitCol,
									descriptionStyle, alignTopStyle, boxStyle, itemAbstractMap.get(item.getItemNumber()));
						}else{
							//if this is not a leaf item in hyrarchy then write only item number and description.
							XSSFRow row = xsheet.createRow(currentRow++);
							ExcelUtill.writeCellValue(item.getItemNumber(),
									row.createCell(scheduleRange.getrItemNum().getFirstColNum()),boxStyle);
							ExcelUtill.writeCellValue(item.getDescription(),
									row.createCell(scheduleRange.getrDescriptionOfItem().getFirstColNum()),descriptionStyle);
							ExcelUtill.writeCellValue(null, row.createCell(quantityCol), boxStyle);
							ExcelUtill.writeCellValue(null, row.createCell(unitCol), boxStyle);
							if(!msheet.isFirstAndFinalBill()){
								RunningBillScheduleRange range = (RunningBillScheduleRange)scheduleRange;
								ExcelUtill.writeCellValue(null, row.createCell(range.getrFullRate().getFirstColNum()), boxStyle);
								if(!msheet.isIsFinalBill()){
									ExcelUtill.writeCellValue(null, row.createCell(range.getrPartRate().getFirstColNum()), boxStyle);
								} 
								ExcelUtill.writeCellValue(null, row.createCell(range.getrUpToDate().getFirstColNum()), boxStyle);
								ExcelUtill.writeCellValue(null, row.createCell(range.getrUptoPreviousBill().getFirstColNum()), boxStyle);
								ExcelUtill.writeCellValue(null, row.createCell(range.getrUptoPreviousBill().getFirstColNum()), boxStyle);
								ExcelUtill.writeCellValue(null, row.createCell(range.getrSincePreviousBill().getFirstColNum()), boxStyle);
							}else{
								ExcelUtill.writeCellValue(null, row.createCell(scheduleRange.getrRate().getFirstColNum()), boxStyle);
								ExcelUtill.writeCellValue(null, row.createCell(scheduleRange.getrPaymentOnActualMeas().getFirstColNum()), boxStyle);
							}
						}
					}else{
						extraItemAbstract.add(itemAbstractMap.get(item.getItemNumber()));
						extraItems.add(item);
					}
				
				} catch (Exception e) {
					logger.error("error writing schedule data for item: "+item.getItemNumber()+"\r\n"+e.getMessage(), e);
					throw new Exception("error writing schedule data for item: "+item.getItemNumber()+"\r\n"+e.getMessage(), e);
				}
			}
			//Write totals before extra item entries for payment since previous bill
			currentRow = writeTotalDataBeforeExtraItem(currentRow, xsheet, itemDescCol,
					scheduleRange.getrTotalAmount().getFirstColNum(), firstRow,
					msheet);
			//Write itemabstract data for Extra items. 
			int itemsTotalRow = currentRow;
			for(Item item : extraItems){
				try {

					if(item.isValidItem()){
						currentRow = writeScheduleData(msheet, xsheet, scheduleRange,
								currentRow, itemNumCol, itemDescCol, quantityCol, unitCol,
								descriptionStyle, alignTopStyle, boxStyle, itemAbstractMap.get(item.getItemNumber()));
					}else{//if this is not the leaf item then write item number and description only.
						//if this is not a leaf item in hyrarchy then write only item number and description.
						XSSFRow row = xsheet.createRow(currentRow++);
						ExcelUtill.writeCellValue(item.getItemNumber(),
								row.createCell(scheduleRange.getrItemNum().getFirstColNum()),alignTopStyle);
						ExcelUtill.writeCellValue(item.getDescription(),
								row.createCell(scheduleRange.getrDescriptionOfItem().getFirstColNum()),descriptionStyle);
						ExcelUtill.writeCellValue(null, row.createCell(quantityCol), boxStyle);
						ExcelUtill.writeCellValue(null, row.createCell(unitCol), boxStyle);
						if(!msheet.isFirstAndFinalBill()){
							RunningBillScheduleRange range = (RunningBillScheduleRange)scheduleRange;
							ExcelUtill.writeCellValue(null, row.createCell(range.getrFullRate().getFirstColNum()), boxStyle);
							if(!msheet.isIsFinalBill()){
								ExcelUtill.writeCellValue(null, row.createCell(range.getrPartRate().getFirstColNum()), boxStyle);
							} 
							ExcelUtill.writeCellValue(null, row.createCell(range.getrUpToDate().getFirstColNum()), boxStyle);
							ExcelUtill.writeCellValue(null, row.createCell(range.getrUptoPreviousBill().getFirstColNum()), boxStyle);
							ExcelUtill.writeCellValue(null, row.createCell(range.getrUptoPreviousBill().getFirstColNum()), boxStyle);
							ExcelUtill.writeCellValue(null, row.createCell(range.getrSincePreviousBill().getFirstColNum()), boxStyle);
						}else{
							ExcelUtill.writeCellValue(null, row.createCell(scheduleRange.getrRate().getFirstColNum()), boxStyle);
							ExcelUtill.writeCellValue(null, row.createCell(scheduleRange.getrPaymentOnActualMeas().getFirstColNum()), boxStyle);
						}
					}
				
				} catch (Exception e) {
					logger.error("error writing schedule data for item: "+item.getItemNumber()+"\r\n"+e.getMessage(), e);
					throw new Exception("error writing schedule data for item: "+item.getItemNumber()+"\r\n"+e.getMessage(), e);
				}
			}
			//Write total data after extra items.
			if(extraItemAbstract.size()>0){
				XSSFRow totalRow = xsheet.createRow(currentRow++);
				XSSFCell labelCell = totalRow.createCell(itemDescCol);
				
				//Add border top.
				totalRow.createCell(itemNumCol).setCellStyle(ExcelUtill.getBorderTop(xsheet.getWorkbook()));
				ExcelUtill.writeCellValue(AbstractSheetConstants.TOTAL_LABEL, labelCell, boldStyle);
				XSSFCell valueCell;
				writeTotalDataAfterExtraItems(currentRow, amountCol, boldStyle,
						itemsTotalRow, totalRow);
				if(!msheet.isFirstAndFinalBill()){
					writeTotalDataAfterExtraItems(currentRow, amountCol-1, boldStyle,
							itemsTotalRow, totalRow);
					writeTotalDataAfterExtraItems(currentRow, amountCol-2, boldStyle,
							itemsTotalRow, totalRow);
				}
				//Write SayRs and round the final value by 0
				totalRow = xsheet.createRow(currentRow++);
				labelCell = totalRow.createCell(itemDescCol);
				ExcelUtill.writeCellValue(AbstractSheetConstants.TOTAL_ROUND_FIGURE_LABLE, labelCell, boldStyle);
				
				writeRoudFigureAfterExtraItemsAndTotal(currentRow, amountCol,
						boldStyle, totalRow);
				if(!msheet.isFirstAndFinalBill()){
					writeRoudFigureAfterExtraItemsAndTotal(currentRow, amountCol-1,
							boldStyle, totalRow);
					writeRoudFigureAfterExtraItemsAndTotal(currentRow, amountCol-2,
							boldStyle, totalRow);
				}
				
			}
			//TODO : 
			/*currentRow = writePreviousBillData(currentRow, amountCol,
					boldStyle, msheet, xsheet, itemDescCol);*/
			
		
		} catch (Exception e) {
			logger.error("error writing schedule report \r\n"+e.getMessage(), e);
			throw new Exception("error writing schedule report \r\n"+e.getMessage(), e);
		}
	}

	

	private int writePreviousBillData(int currentRow, int amountCol,
			XSSFCellStyle boldStyle, MeasurementSheet msheet, XSSFSheet xsheet, int itemDescCol) throws Exception {
		// TODO Auto-generated method stub
		XSSFRow createdRow = xsheet.createRow(currentRow++);
		XSSFCell labelCell = createdRow.createCell(itemDescCol);
		String alreadyPaidInPreviousBillLabel = AbstractSheetConstants.ALREADY_PAID_IN_PREVIOUS_BILL
				.replace(AbstractSheetConstants.PREVIOUS_BILL,
						"" + msheet.getPreviousMeasurementSheet().getSerialNumberDisplayFormat());
		ExcelUtill.writeCellValue(alreadyPaidInPreviousBillLabel, labelCell, boldStyle);
		
		return currentRow;
	}

	private void writeRoudFigureAfterExtraItemsAndTotal(int currentRow,
			int amountCol, XSSFCellStyle boldStyle, XSSFRow totalRow)
			throws Exception {
		XSSFCell valueCell;
		valueCell = totalRow.createCell(amountCol);
		ExcelUtill.writeCellValue("=ROUND("+ExcelUtill.intToChar(amountCol) + (currentRow-1)+", 0)", valueCell, boldStyle);
	}

	private void writeTotalDataAfterExtraItems(int currentRow, int amountCol,
			XSSFCellStyle boldStyle, int itemsTotalRow, XSSFRow totalRow)
			throws Exception {
		XSSFCell valueCell = totalRow.createCell(amountCol);
		ExcelUtill.writeCellValue("=SUM("
				+ ExcelUtill.intToChar(amountCol) + itemsTotalRow + ":"
				+ ExcelUtill.intToChar(amountCol) + (currentRow-1)+ ")", valueCell, boldStyle);
	}

	private int writeScheduleData(MeasurementSheet msheet, XSSFSheet xsheet,
			ScheduleRange scheduleRange, int currentRow, int itemNumCol,
			int itemDescCol, int quantityCol, int unitCol,
			XSSFCellStyle descriptionStyle, XSSFCellStyle alignTopStyle,
			XSSFCellStyle boxStyle, ItemAbstract itemAbstract) throws Exception {
		XSSFRow row = xsheet.createRow(currentRow++);
		Item item = itemAbstract.getItem();
		//Item numner
		ExcelUtill.writeCellValue(item.getItemNumber(), row.createCell(itemNumCol), alignTopStyle);
		//Item Description
		ExcelUtill.writeCellValue(item.getDescription(), row.createCell(itemDescCol), descriptionStyle);
		ExcelUtill.writeCellValue("="+itemAbstract.getAbsCellRef(), row.createCell(quantityCol), boxStyle);
		ExcelUtill.writeCellValue(item.getUnit(), row.createCell(unitCol), boxStyle);
		if(!msheet.isFirstAndFinalBill()){
			RunningBillScheduleRange range = (RunningBillScheduleRange)scheduleRange;
			ExcelUtill.writeCellValue(this.getFullRateRefFormula(itemAbstract), row.createCell(range.getrFullRate().getFirstColNum()), boxStyle);
			if(!msheet.isIsFinalBill()){
				ExcelUtill.writeCellValue(this.getPartRateRefFormula(itemAbstract), row.createCell(range.getrPartRate().getFirstColNum()), boxStyle);
				
			} 
			ExcelUtill.writeCellValue(
					getPaymentUptoDataFormula(range, currentRow, msheet
							.isIsFinalBill(), item.getQuantityPerUnit()), row.createCell(range.getrUpToDate().getFirstColNum()), boxStyle);
			
			if(msheet.getPreviousMeasurementSheet()!=null && msheet.getPreviousItemAbstract(
					item)!=null){
				ExcelUtill.writeCellValue(
						msheet.getPreviousItemAbstract(
								item).getAmount(), row.createCell(
										range.getrUptoPreviousBill().getFirstColNum()), boxStyle);
				
			}else{
				ExcelUtill.writeCellValue(new Integer(0), row.createCell(range.getrUptoPreviousBill().getFirstColNum()), boxStyle);
			}
			ExcelUtill.writeCellValue(
					getPreviousBillAmountDifferenceFormula(
							currentRow, range), row.createCell(range.getrSincePreviousBill().getFirstColNum()), boxStyle);
			
		}else{
			ExcelUtill.writeCellValue(getFullRateRefFormula(itemAbstract), row.createCell(scheduleRange.getrRate().getFirstColNum()), boxStyle);
			ExcelUtill.writeCellValue(getPaymentOfActualMeasurementFormula(scheduleRange, currentRow, item.getQuantityPerUnit()), row.createCell(scheduleRange.getrPaymentOnActualMeas().getFirstColNum()), boxStyle);
		}
		return currentRow;
	} 
	
	private int writeTotalDataBeforeExtraItem(int rowNum, XSSFSheet scheduleSheet, int descCol, int amountCol, int firstRow, MeasurementSheet msheet) throws Exception{
		XSSFRow currentRow = scheduleSheet.createRow(rowNum++);
		XSSFCell labelCell = currentRow.createCell(descCol);
		XSSFCell valueCell = currentRow.createCell(amountCol);
		XSSFCellStyle boldStyle = ExcelUtill.getBoldFont(scheduleSheet.getWorkbook());
		//Add border top.
		currentRow.createCell(descCol-1).setCellStyle(ExcelUtill.getBorderTop(scheduleSheet.getWorkbook()));
		//write total
		ExcelUtill.writeCellValue(AbstractSheetConstants.TOTAL_LABEL, labelCell,boldStyle);
		
		//write below data for each columns  of payment "Up to Date" "Upto Previous Bill" "Since Previous Bill"
		ExcelUtill.writeCellValue(getQuantityFormula(firstRow, rowNum, amountCol), valueCell,boldStyle);
		if(!msheet.isFirstAndFinalBill()){
			valueCell = currentRow.createCell(amountCol-1);
			ExcelUtill.writeCellValue(getQuantityFormula(firstRow, rowNum, amountCol-1), valueCell,boldStyle);
			valueCell = currentRow.createCell(amountCol-2);
			ExcelUtill.writeCellValue(getQuantityFormula(firstRow, rowNum, amountCol-2), valueCell,boldStyle);
			
		}
		
		
		//write cost index
		if (msheet.getAggreement().getCostIndex() != 0) {
			currentRow = scheduleSheet.createRow(rowNum++);
			labelCell = currentRow.createCell(descCol);
			ExcelUtill.writeCellValue(
					AbstractSheetConstants.COST_INDEX_LABEL
					+ msheet.getAggreement().getCostIndex() + "%", labelCell,boldStyle);
			//=ROUND((G18)*14%,2)
			
			addCostIndexData(rowNum, amountCol, msheet, currentRow, boldStyle);
			if(!msheet.isFirstAndFinalBill()){
				addCostIndexData(rowNum, amountCol-1, msheet, currentRow, boldStyle);
				addCostIndexData(rowNum, amountCol-2, msheet, currentRow, boldStyle);
				
			}
			//add cost index
			currentRow = scheduleSheet.createRow(rowNum++);
			labelCell = currentRow.createCell(descCol);
			ExcelUtill.writeCellValue(AbstractSheetConstants.TOTAL_LABEL, labelCell,boldStyle);
			addTotalAfterAddingCostIndex(rowNum, amountCol, currentRow,
					boldStyle);
			if(!msheet.isFirstAndFinalBill()){
				addTotalAfterAddingCostIndex(rowNum, amountCol-1, currentRow,
						boldStyle);
				addTotalAfterAddingCostIndex(rowNum, amountCol-2, currentRow,
								boldStyle);
			}
		}
		if(msheet.getAggreement().getClausePercentage()>0){
			currentRow = scheduleSheet.createRow(rowNum++);
			labelCell = currentRow.createCell(descCol);
			
			String ContractorAbatmentLabel = AbstractSheetConstants.ADD_CONTRACTOR_ABATEMENT
					.replace(AbstractSheetConstants.ADD_CONTRACTOR_PERCENT,
							"" + msheet.getAggreement().getClausePercentage())
					.replace(AbstractSheetConstants.ADD_CONTRACTOR_CLAUSE,
							msheet.getAggreement().getClause().toString());
			ExcelUtill.writeCellValue(ContractorAbatmentLabel, labelCell,boldStyle);
			addContractorsAbatmentData(rowNum, amountCol, msheet, currentRow,
					boldStyle);
			if(!msheet.isFirstAndFinalBill()){
				addContractorsAbatmentData(rowNum, amountCol-1, msheet, currentRow,
						boldStyle);
				addContractorsAbatmentData(rowNum, amountCol-2, msheet, currentRow,
						boldStyle);
			}
			//add contractors abbattment
			currentRow = scheduleSheet.createRow(rowNum++);
			labelCell = currentRow.createCell(descCol);
			ExcelUtill.writeCellValue(AbstractSheetConstants.TOTAL_LABEL, labelCell,boldStyle);
			
			addTotalDataAfterContractorsAbbatment(rowNum, amountCol,
					currentRow, boldStyle);
			if(!msheet.isFirstAndFinalBill()){
				addTotalDataAfterContractorsAbbatment(rowNum, amountCol-1,
						currentRow, boldStyle);
				addTotalDataAfterContractorsAbbatment(rowNum, amountCol-2,
						currentRow, boldStyle);
			}
		}
		return rowNum;
	}

	private void addTotalDataAfterContractorsAbbatment(int rowNum,
			int amountCol, XSSFRow currentRow, XSSFCellStyle boldStyle)
			throws Exception {
		XSSFCell valueCell;
		valueCell = currentRow.createCell(amountCol);
		ExcelUtill.writeCellValue("=SUM("
				+ ExcelUtill.intToChar(amountCol) + (rowNum-1) + ":"
				+ ExcelUtill.intToChar(amountCol) + (rowNum-2) + ")", valueCell,boldStyle);
	}

	private void addContractorsAbatmentData(int rowNum, int amountCol,
			MeasurementSheet msheet, XSSFRow currentRow, XSSFCellStyle boldStyle)
			throws Exception {
		XSSFCell valueCell;
		valueCell = currentRow.createCell(amountCol);
		ExcelUtill.writeCellValue(
				getContractorsAbbatmentFormula(msheet.getAggreement()
						.getClause(), msheet.getAggreement()
						.getClausePercentage(), amountCol, rowNum),
				valueCell, boldStyle);
	}

	private void addTotalAfterAddingCostIndex(int rowNum, int amountCol,
			XSSFRow currentRow, XSSFCellStyle boldStyle) throws Exception {
		XSSFCell valueCell;
		addTotalDataAfterContractorsAbbatment(rowNum, amountCol, currentRow,
				boldStyle);
	}

	private void addCostIndexData(int rowNum, int amountCol,
			MeasurementSheet msheet, XSSFRow currentRow, XSSFCellStyle boldStyle)
			throws Exception {
		XSSFCell valueCell;
		valueCell = currentRow.createCell(amountCol);
		ExcelUtill.writeCellValue(
				"=ROUND((" + ExcelUtill.intToChar(amountCol) + (rowNum-1)
						+ ")*" + msheet.getAggreement().getCostIndex()
						+ "%,2)", valueCell,boldStyle);
	}
	
	private String getQuantityFormula(int firstRow, int lastRow, int quantityCol){
		String f1 = ExcelUtill.intToChar(quantityCol)+""+(firstRow+1);
		String f2 = ExcelUtill.intToChar(quantityCol)+""+(lastRow-1);
		String formula = "=SUM("+f1+":"+f2+")";
		return formula;
	}
	
	private String getContractorsAbbatmentFormula(Clause clause, float percentg, int amountCol, int rowNum){
		String f1 = clause == Clause.Below ? "-" : "";
		return "="+f1+"ROUND("
				+ ExcelUtill.intToChar(amountCol) + (rowNum-1) + "*"
				+ percentg + "%,2)";
	}
	
	private String getPreviousBillAmountDifferenceFormula(int row, RunningBillScheduleRange range){
		String f1 = ExcelUtill.intToChar(range.getrUpToDate().getFirstColNum()) + "" + (row);
		String f2 = ExcelUtill.intToChar(range.getrUptoPreviousBill().getFirstColNum()) + "" + (row);
		return "=ROUND(("+f1+"-"+f2+"),2)";
	}
	
	
	private String getPaymentUptoDataFormula(RunningBillScheduleRange range,int row, boolean isFinalBill, double quantityPerUnit){
		//=ROUND(D20*G20,2)
		String f1 = ExcelUtill.intToChar(range.getrQuantExecUptoDate().getFirstColNum()) + "" + (row);
		String f2 = "";
		if(isFinalBill){
			f2 = ExcelUtill.intToChar(range.getrFullRate().getFirstColNum()) + "" + (row);
		}else{
			f2 = ExcelUtill.intToChar(range.getrPartRate().getFirstColNum()) + "" + (row);
		}
		return "=ROUND(("+f1+"*"+f2+")/"+quantityPerUnit+",2)";
	}
	
	private String getPaymentOfActualMeasurementFormula(ScheduleRange range,int row, double quantityPerUnit){
		//=ROUND(D20*G20,2)
		String f1 = ExcelUtill.intToChar(range.getrQuantExecUptoDate().getFirstColNum()) + "" + (row);
		String f2 = ExcelUtill.intToChar(range.getrRate().getFirstColNum()) + "" + (row);
		return "=ROUND(("+f1+"*"+f2+")/"+quantityPerUnit+",2)";
	}
	
	private String getPartRateRefFormula(ItemAbstract itemAbstract){
		return "="+Worksheets.ABSTRACTSHEET+"!"+itemAbstract.getPartRateRef();
	}
	
	private String getFullRateRefFormula(ItemAbstract itemAbstract){
		return "="+Worksheets.ABSTRACTSHEET+"!"+itemAbstract.getFullRateRef();
	}
	
	private XSSFSheet getScheduleSheet(MeasurementSheet msheet, XSSFWorkbook wb){
		if(msheet.isFirstAndFinalBill()){
			return wb.getSheet(Worksheets.FNFB_SCHEDULE);
		}else{
			return wb.getSheet(Worksheets.RB_SCHEDULE); 
		}
	}
	
	public XSSFSheet prepareScheduleSheet(MeasurementSheet msheet, XSSFWorkbook wb){
		XSSFSheet xsheet = null;
		RunningBillScheduleRange rbScheduleRange = new RunningBillScheduleRange(wb);
		if(msheet.isFirstAndFinalBill()){
			xsheet = wb.getSheet(Worksheets.FNFB_SCHEDULE);
			//hide other sheet
			wb.setSheetHidden(wb.getSheetIndex(Worksheets.RB_SCHEDULE), true);
			//unhide required sheet and columns
			wb.setSheetHidden(wb.getSheetIndex(Worksheets.FNFB_SCHEDULE), false);
		}else{
			xsheet = wb.getSheet(Worksheets.RB_SCHEDULE);
			//if this is a final bill then hide part rate column
			if(msheet.isIsFinalBill()){
				xsheet.setColumnHidden((rbScheduleRange).getrPartRate().getFirstColNum(), true);
			}else{
				xsheet.setColumnHidden((rbScheduleRange).getrPartRate().getFirstColNum(), false);
			}
			//hide other sheet
			wb.setSheetHidden(wb.getSheetIndex(Worksheets.FNFB_SCHEDULE), true);
			wb.setSheetHidden(wb.getSheetIndex(Worksheets.RB_SCHEDULE), false);
		}
		return xsheet;
		
	} 
	
	private ScheduleRange getScheduleRange(MeasurementSheet msheet, XSSFWorkbook wb){
		if(msheet.isFirstAndFinalBill()){
			return new ScheduleRange(wb);
		}else{
			return new RunningBillScheduleRange(wb);
		}
	}
}
