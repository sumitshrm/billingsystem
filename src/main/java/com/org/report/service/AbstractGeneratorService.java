package com.org.report.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.org.constants.AbstractSheetConstants;
import com.org.constants.CellIdentifier;
import com.org.constants.MeasurementSheetConstants;
import com.org.constants.Worksheets;
import com.org.entity.Aggreement;
import com.org.entity.Item;
import com.org.entity.ItemAbstract;
import com.org.entity.ItemAbstractDataTo;
import com.org.entity.MeasurementSheet;
import com.org.excel.service.ExcelUtill;
import com.org.excel.util.AbstractData;
import com.org.excel.util.AbstractRanges;
import com.org.excel.util.MasterDataCellName;
import com.org.excel.util.XLColumnRange;
import com.org.util.Clause;

@Service(value="abstractGeneratorService")
public class AbstractGeneratorService implements IExcelReportService{
	
	private static int ABSTRACT_ENTRY_TYPE_1 = 1;
	
	private static int ABSTRACT_ENTRY_TYPE_2 = 2;
	
	private static int ABSTRACT_ENTRY_TYPE_3 = 3;
	
	private static int ABSTRACT_ENTRY_TYPE_4 = 4;
	
	private static String QUOTES = "\"";
	
	final static Logger logger = Logger.getLogger(AbstractGeneratorService.class);
	
	public void generateMasterData(MeasurementSheet msheet, XSSFWorkbook wb) throws Exception {
		System.out.println("writing abstract sheet master data");
		XSSFSheet xsheet = wb.getSheet(Worksheets.ABSTRACTSHEET);
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.AGGREEMENT_NO, msheet.getAggreement().getAggreementNum());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.NAME_OF_WORK, msheet.getAggreement().getNameOfWork());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.AGENCY, msheet.getAggreement().getAgency());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_START, msheet.getAggreement().getDateOfStart());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_COMPLETION_S, msheet.getAggreement().getDateOfCompletionS());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_COMPLETION_A, msheet.getAggreement().getDateOfCompletionA());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.SNO_OF_BILL, msheet.getSerialNumberDisplayFormat());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.CLAUSE, msheet.getAggreement().getClausePercentage() + "% " + msheet.getAggreement().getClause());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.DATE_OF_ABSTRACT, msheet.getAggreement().getDateOfAbstract());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.TENDER_COST, msheet.getAggreement().getTenderCost());
		ExcelUtill.writeMasterData(xsheet, MasterDataCellName.ESTIMATED_COST, msheet.getAggreement().getEstimatedCost()); 
		prepareAbstractSheet(msheet, wb, xsheet);
	}
	
	public void prepareAbstractSheet(MeasurementSheet msheet, XSSFWorkbook wb, XSSFSheet xsheet) {
		// if this is a final bill then remove part rate column
		XLColumnRange rPartRate = new XLColumnRange(wb,
				AbstractSheetConstants.RANGE_ABSTRACT_PART_RATE);
		if (msheet.isIsFinalBill()) {
			xsheet.setColumnHidden(rPartRate.getFirstColNum(), true);
		}else{
			xsheet.setColumnHidden(rPartRate.getFirstColNum(), false);
		}
	}
	
	public void removeReportData(MeasurementSheet msheet, XSSFWorkbook workbook){
		AbstractRanges abstractRange = new AbstractRanges(workbook);
		XSSFSheet abstractSheet = workbook.getSheet(Worksheets.ABSTRACTSHEET);
		ExcelUtill.deleteRow(abstractSheet, abstractRange.getrItemNumber().getFirstRowNum()+1, abstractSheet.getLastRowNum());
	}
	
	public void generateReport(MeasurementSheet msheet, XSSFWorkbook workbook) throws Exception{
		// create the map of items and extra items
		removeReportData(msheet, workbook);
		prepareItemAbstractData(msheet);
		loadItemAbstractDataFromWorkbook(workbook, msheet);
		writeAbstractToWorkbook(msheet, workbook);
		//updateItemAbstractTotalAndReferences(msheet);
		msheet.persist();

	}
	
	protected void prepareItemAbstractData(MeasurementSheet msheet) {
		List<Item> items = new ArrayList<>(msheet.getAggreement().getItems());
		List<Item> itemsWithItemAbstract = new ArrayList<Item>();
		for(ItemAbstract abs : msheet.getItemAbstracts()){
			itemsWithItemAbstract.add(abs.getItem());
		}
		items.removeAll(itemsWithItemAbstract);
		for(Item item : items){
			if(item.isValidItem()){
				ItemAbstract abs = new ItemAbstract(msheet, item);
				abs.persist();
				msheet.getItemAbstracts().add(abs);
			}
		}
	}

	public void reloadData(MeasurementSheet msheet, XSSFWorkbook workbook) throws Exception{
		prepareItemAbstractData(msheet);
		loadItemAbstractDataFromWorkbook(workbook, msheet);
		updateItemAbstractTotalAndReferences(msheet);
		msheet.persist();
	}
	
	protected void updateItemAbstractTotalAndReferences(MeasurementSheet msheet) {
		MeasurementSheet prevMeasurementSheet = msheet.getPreviousMeasurementSheet();
		for(ItemAbstract itemAbstract : msheet.getItemAbstracts()){
			double total = 0.0;
			for(ItemAbstractDataTo data : itemAbstract.getItemDataTos()){
				total+=data.getTotal();
			}
			if(prevMeasurementSheet!=null){
				ItemAbstract previousAbstract = prevMeasurementSheet.getItemAbstractByItemNum(
						itemAbstract.getItem().getItemNumber());
				if(previousAbstract!=null){
					total += previousAbstract.getTotal();
				}
				
			}
			itemAbstract.setTotal(total);
		}
	}

	protected void loadItemAbstractDataFromWorkbook(XSSFWorkbook workbook, MeasurementSheet msheet) {
		XSSFSheet sheet = workbook.getSheet(Worksheets.MEASUREMENTSHEET);
		// Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();
		// Traversing over each row of XLSX file
		String itemNumber = null;
		boolean isSelectedCellItemNumber = true;
		//int metadata_column = getMetaDataColumnNumber(workbook);
		int metadata_column = ExcelUtill.getColumnNumberByRangeName(workbook, MeasurementSheetConstants.M_ITEM_NUM);
		System.out.println("metadata column : " + metadata_column);
		ItemAbstract itemAbs = null;
		ItemAbstractDataTo absData = null;
		while (rowIterator.hasNext()) {
			
			Row row = rowIterator.next();
			String itemNum = ExcelUtill.getCellValueAsString(row.getCell(metadata_column));
			System.out.println("item num -------------"+itemNum);
			if (!itemNum.trim().equals("")
					&& !itemNum
							.equals(MeasurementSheetConstants.MEASUREMENT_TITLES_METADATA_NAME)) {
				if (isSelectedCellItemNumber) {
					itemNumber = itemNum;
				} else {
					
					Cell totalCell = row.getCell(metadata_column);
					itemAbs = msheet.getItemAbstractByItemNum(itemNumber);
					if(itemAbs!=null){
						absData = new ItemAbstractDataTo();
						absData.setTotal(totalCell.getNumericCellValue());
						absData.setMeasCellRef(ExcelUtill.getCellReference(totalCell));
						itemAbs.getItemDataTos().add(absData);
					}
				}
				isSelectedCellItemNumber = !isSelectedCellItemNumber;
			}
		
		}
		
	}
	
	protected void writeAbstractToWorkbook(MeasurementSheet msheet,
			XSSFWorkbook workbook) throws Exception {
		XSSFSheet abstractSheet = workbook.getSheet(Worksheets.ABSTRACTSHEET);
		AbstractRanges abstractRange = new AbstractRanges(workbook);
		XSSFCellStyle boldStyle = abstractRange.getBoldStyle();
		int currentRow = abstractSheet.getLastRowNum() + 1;
		int firstRow = currentRow;
		List<Item> extraItems = new ArrayList<Item>();
		for (Item item : msheet.getAggreement().getItems()) {
			if (item.isValidItem()) {
				if (item.isIsExtraItem()) {
					extraItems.add(item);
				} else {
					try {
						currentRow = writeAbstractData(
								msheet.getItemAbstractByItemNum(item
										.getItemNumber()), abstractSheet,
								currentRow, item, abstractRange);
					}catch(Exception e){
						e.printStackTrace();
						throw new Exception("Exception occured while writing item:"+item
								.getItemNumber());
					}
				}
			}
		}
		//write total data.
		currentRow = writeTotalDataBeforeExtraItem(currentRow, abstractSheet, abstractRange, firstRow, msheet);
		int itemsTotalRow = currentRow;
		//write extra items now
		for(Item item : extraItems){
			currentRow = writeAbstractData(msheet.getItemAbstractByItemNum(item.getItemNumber()), abstractSheet, currentRow,
					item, abstractRange);
		}
		//write total after extra items
		if(extraItems.size()>0){
			XSSFRow totalRow = abstractSheet.createRow(currentRow++);
			XSSFCell labelCell = totalRow.createCell(abstractRange.getDescCol());
			XSSFCell valueCell = totalRow.createCell(abstractRange.getAmountCol());
			//Add border top.
			totalRow.createCell(abstractRange.getItemNumCol()).setCellStyle(ExcelUtill.getBorderTop(abstractSheet.getWorkbook()));
			ExcelUtill.writeCellValue(AbstractSheetConstants.TOTAL_LABEL, labelCell, boldStyle);
			ExcelUtill.writeCellValue("=SUM("
					+ ExcelUtill.intToChar(abstractRange.getAmountCol()) + itemsTotalRow + ":"
					+ ExcelUtill.intToChar(abstractRange.getAmountCol()) + (currentRow-1)+ ")", valueCell, boldStyle);
			//Write SayRs and round the final value by 0
			totalRow = abstractSheet.createRow(currentRow++);
			labelCell = totalRow.createCell(abstractRange.getDescCol());
			valueCell = totalRow.createCell(abstractRange.getAmountCol());
			ExcelUtill.writeCellValue(AbstractSheetConstants.TOTAL_ROUND_FIGURE_LABLE, labelCell, boldStyle);
			ExcelUtill.writeCellValue("=ROUND("+ExcelUtill.intToChar(abstractRange.getAmountCol()) + (currentRow-1)+", 0)", valueCell, boldStyle);
			
		}
	}
	
	protected int writeTotalDataBeforeExtraItem(int rowNum, XSSFSheet abstractSheet, AbstractRanges abstractRange, int firstRow, MeasurementSheet msheet) throws Exception{
		XSSFRow currentRow = abstractSheet.createRow(rowNum++);
		XSSFCell labelCell = currentRow.createCell(abstractRange.getDescCol());
		XSSFCell valueCell = currentRow.createCell(abstractRange.getAmountCol());
		XSSFCellStyle boldStyle = abstractRange.getBoldStyle();
		//Add border top.
		currentRow.createCell(abstractRange.getDescCol()-1).setCellStyle(ExcelUtill.getBorderTop(abstractSheet.getWorkbook()));
		//write total
		ExcelUtill.writeCellValue(AbstractSheetConstants.TOTAL_LABEL, labelCell,boldStyle);
		ExcelUtill.writeCellValue(getQuantityFormula(firstRow, rowNum, abstractRange.getAmountCol()), valueCell,boldStyle);
		//write cost index
		if (msheet.getAggreement().getCostIndex() != 0) {
			currentRow = abstractSheet.createRow(rowNum++);
			labelCell = currentRow.createCell(abstractRange.getDescCol());
			valueCell = currentRow.createCell(abstractRange.getAmountCol());
			ExcelUtill.writeCellValue(
					AbstractSheetConstants.COST_INDEX_LABEL
					+ msheet.getAggreement().getCostIndex() + "%", labelCell,boldStyle);
			//=ROUND((G18)*14%,2)
			ExcelUtill.writeCellValue(
					"=ROUND((" + ExcelUtill.intToChar(abstractRange.getAmountCol()) + (rowNum-1)
							+ ")*" + msheet.getAggreement().getCostIndex()
							+ "%,2)", valueCell,boldStyle);
			//add cost index
			currentRow = abstractSheet.createRow(rowNum++);
			labelCell = currentRow.createCell(abstractRange.getDescCol());
			valueCell = currentRow.createCell(abstractRange.getAmountCol());
			ExcelUtill.writeCellValue(AbstractSheetConstants.TOTAL_LABEL, labelCell,boldStyle);
			ExcelUtill.writeCellValue("=SUM("
					+ ExcelUtill.intToChar(abstractRange.getAmountCol()) + (rowNum-1) + ":"
					+ ExcelUtill.intToChar(abstractRange.getAmountCol()) + (rowNum-2) + ")", valueCell,boldStyle);
		}
		if(msheet.getAggreement().getClausePercentage()>0){
			currentRow = abstractSheet.createRow(rowNum++);
			labelCell = currentRow.createCell(abstractRange.getDescCol());
			valueCell = currentRow.createCell(abstractRange.getAmountCol());
			String ContractorAbatmentLabel = AbstractSheetConstants.ADD_CONTRACTOR_ABATEMENT
					.replace(AbstractSheetConstants.ADD_CONTRACTOR_PERCENT,
							"" + msheet.getAggreement().getClausePercentage())
					.replace(AbstractSheetConstants.ADD_CONTRACTOR_CLAUSE,
							msheet.getAggreement().getClause().toString());
			ExcelUtill.writeCellValue(ContractorAbatmentLabel, labelCell,boldStyle);
			ExcelUtill.writeCellValue(
					getContractorsAbbatmentFormula(msheet.getAggreement()
							.getClause(), msheet.getAggreement()
							.getClausePercentage(), abstractRange.getAmountCol(), rowNum),
					valueCell, boldStyle);
			//add contractors abbattment
			currentRow = abstractSheet.createRow(rowNum++);
			labelCell = currentRow.createCell(abstractRange.getDescCol());
			valueCell = currentRow.createCell(abstractRange.getAmountCol());
			ExcelUtill.writeCellValue(AbstractSheetConstants.TOTAL_LABEL, labelCell,boldStyle);
			ExcelUtill.writeCellValue("=SUM("
					+ ExcelUtill.intToChar(abstractRange.getAmountCol()) + (rowNum-1) + ":"
					+ ExcelUtill.intToChar(abstractRange.getAmountCol()) + (rowNum-2) + ")", valueCell,boldStyle);
		}
		return rowNum;
	}

	protected int writeAbstractData(ItemAbstract itemAbs,
			XSSFSheet abstractSheet, int rowNum, Item item, AbstractRanges abstractRange) throws Exception {
		logger.info("ItemAbstract : "+ itemAbs.getItem().getItemNumber() +", ItemNum : "+item.getItemNumber()+", rowNum : "+rowNum);
		int firstRow = rowNum;
		XSSFRow row = abstractSheet.createRow(rowNum++);
		XSSFCell hyperlinkCell = null;
		XSSFCellStyle boxStyle = abstractRange.getBoxStyle();
		XSSFCellStyle boxAndBoldStyle = abstractRange.getBoxAndBoldStyle();
		XSSFCellStyle borderTopRightStyle = abstractRange.getBorderTopRightStyle();
		XSSFCellStyle descriptionCellStyle = abstractRange.getDescriptionCellStyle();
		//Write item number and description in header row.
		ExcelUtill.writeCellValue(item.getItemNumber(),
				row.createCell(abstractRange.getItemNumCol()),borderTopRightStyle);
		ExcelUtill.writeCellValue(item.getFullDescription(),
				row.createCell(abstractRange.getDescCol()),descriptionCellStyle);
		row.createCell(abstractRange.getQtyCol()).setCellStyle(boxStyle);
		row.createCell(abstractRange.getUnitCol()).setCellStyle(boxStyle);
		row.createCell(abstractRange.getFullRateCol()).setCellStyle(boxStyle);
		row.createCell(abstractRange.getPartRateCol()).setCellStyle(boxStyle);
		row.createCell(abstractRange.getAmountCol()).setCellStyle(boxStyle);
		double abstractTotal = 0.0;
		// check if amount from previous bill is available
		MeasurementSheet prevMsheet = itemAbs.getMeasurementSheet().getPreviousMeasurementSheet() ;
		if (prevMsheet != null) {
			ItemAbstract prevAbstract = prevMsheet.getItemAbstractByItemNum(item.getItemNumber());
			if (prevAbstract != null && prevAbstract.getTotal() > 0) {
				row = abstractSheet.createRow(rowNum++);
				ExcelUtill.writeCellValue(getPreviousItemAbstractDescription(prevMsheet.getSerialNumberDisplayFormat()),
								row.createCell(abstractRange.getDescCol()) , boxStyle);
				ExcelUtill.writeCellValue(prevAbstract.getTotal(),
						row.createCell(abstractRange.getQtyCol()), boxStyle);
				ExcelUtill.writeCellValue(item.getUnit(),
						row.createCell(abstractRange.getUnitCol()), boxStyle);
				row.createCell(abstractRange.getFullRateCol()).setCellStyle(boxStyle);
				row.createCell(abstractRange.getPartRateCol()).setCellStyle(boxStyle);
				row.createCell(abstractRange.getAmountCol()).setCellStyle(boxStyle);
				abstractTotal +=prevAbstract.getTotal();
			}
		}
		
		if (itemAbs != null) {
			for (ItemAbstractDataTo itemAbsData : itemAbs.getItemDataTos()) {
				row = abstractSheet.createRow(rowNum++);
				row.createCell(abstractRange.getItemNumCol());
				hyperlinkCell = row.createCell(abstractRange.getDescCol());
				ExcelUtill.writeCellValue(
						getDescriptionFormula(rowNum, abstractRange.getMetadataPageNumCol(), Worksheets.MEASUREMENTSHEET+"!"+itemAbsData.getMeasCellRefHyperlink()),
						hyperlinkCell , boxStyle);
				XSSFCell abstractTotalCell = row.createCell(abstractRange.getQtyCol());
				ExcelUtill.writeCellValue("="+Worksheets.MEASUREMENTSHEET+"!"+itemAbsData.getMeasCellRef(),
						abstractTotalCell, boxStyle);
				itemAbsData.setAbsCellRef(ExcelUtill.getCellReference(abstractTotalCell));
				ExcelUtill.writeCellValue(item.getUnit(),
						row.createCell(abstractRange.getUnitCol()), boxStyle);
				row.createCell(abstractRange.getFullRateCol()).setCellStyle(boxStyle);
				row.createCell(abstractRange.getPartRateCol()).setCellStyle(boxStyle);
				row.createCell(abstractRange.getAmountCol()).setCellStyle(boxStyle);
				//setHyperlinkInMeasurementSheet(abstractSheet.getWorkbook(),Worksheets.MEASUREMENTSHEET+"!"+itemAbsData.getMeasCellRef() , hyperlinkCell, itemAbsData);
				setRefValueInMeasurementSheet(abstractSheet.getWorkbook(),Worksheets.MEASUREMENTSHEET+"!"+itemAbsData.getMeasCellRef() , hyperlinkCell);
				abstractTotal +=itemAbsData.getTotal();
				//Write reference of this item in measurement sheet.
				ExcelUtill.writeCellValue("="+Worksheets.MEASUREMENTSHEET+"!"+itemAbsData.getMeasCellRef(), row.createCell(abstractRange.getMetaDataTotalCol()));
				//write reference of Abstract sheet in measurement she

			}
		}
		row = abstractSheet.createRow(rowNum++);
		//row.createCell(itemnumCol).setCellStyle(boxStyle);
		row.createCell(abstractRange.getDescCol()).setCellStyle(boxStyle);
		ExcelUtill.writeCellValue(
				getQuantityFormula(firstRow, rowNum, abstractRange.getQtyCol()),
				row.createCell(abstractRange.getQtyCol()), boxAndBoldStyle); 
		ExcelUtill.writeCellValue(item.getUnit(),
				row.createCell(abstractRange.getUnitCol()), boxAndBoldStyle);
		ExcelUtill.writeCellValue(item.getFullRate(),
				row.createCell(abstractRange.getFullRateCol()), boxAndBoldStyle);
		ExcelUtill.writeCellValue(item.getPartRate(),
				row.createCell(abstractRange.getPartRateCol()), boxAndBoldStyle);
		ExcelUtill.writeCellValue(
				getAmountFormula(rowNum, abstractRange.getFullRateCol(), abstractRange.getPartRateCol(), abstractRange.getQtyCol(),
						item.getQuantityPerUnit(), itemAbs.getMeasurementSheet().isIsFinalBill()), row
						.createCell(abstractRange.getAmountCol()), boxAndBoldStyle);
		itemAbs.setAbsCellRef(ExcelUtill.getCellReference(row.getCell(abstractRange.getQtyCol())));
		itemAbs.setPartRateRef(ExcelUtill.getCellReference(row.getCell(abstractRange.getPartRateCol())));
		itemAbs.setFullRateRef(ExcelUtill.getCellReference(row.getCell(abstractRange.getFullRateCol())));
		//write part rate meta data
		ExcelUtill.writeCellValue(
				getPartRateFormula(item.getPartRate(), itemAbs.getMeasurementSheet().isIsFinalBill(),
						abstractRange.getPartRateCol(), rowNum, item.getItemNumber()), row
						.createCell(abstractRange.getMetaDataPartRateUpdatedCol()));
		//Set item abstract total data and references.
		itemAbs.setTotal(abstractTotal);
		itemAbs.setAbsCellRef(Worksheets.ABSTRACTSHEET+"!"+ExcelUtill.intToChar(abstractRange.getQtyCol())+rowNum);
		return rowNum;

	}

	

	
	/**
	 * @param workbook
	 * @param rowNum
	 * @param data
	 * @param range
	 * @param type 1=main item description, 2=entry for eact measurement item, 3=total row.
	 * @return
	 * @throws Exception 
	 */
	
	protected String getPartRateFormula(double partRate, boolean isFinalBill, int partRateColNum, int currentRowNum, String itemNumber){
		String f2 = null;
		if(!isFinalBill){
			String f1 = ExcelUtill.intToChar(partRateColNum)+""+(currentRowNum);
			f2 = "=IF("+f1+"="+partRate+","+QUOTES+""+QUOTES+",("+QUOTES+itemNumber+QUOTES+" & "+QUOTES+","+QUOTES+" & "+f1+"))";
		}
		return f2;
	}
	
	protected void setRefValueInMeasurementSheet(XSSFWorkbook workbook,
			String source, Cell target) {
		XSSFSheet msheet = workbook.getSheet(Worksheets.MEASUREMENTSHEET);
		CellReference ref_source = new CellReference(source);
		int namedCellIdx = workbook
				.getNameIndex(MeasurementSheetConstants.MEASUREMENT_ABSTRACT_REF);
		Name aNamedCell = workbook.getNameAt(namedCellIdx);
		int abstract_ref_colnum = new AreaReference(aNamedCell.getRefersToFormula()).getFirstCell().getCol();
		int abstract_ref_rownum = ref_source.getRow();
		String ref_cell = Worksheets.ABSTRACTSHEET + "!$"+CellReference.convertNumToColString(target.getColumnIndex())+"$"+(target.getRow().getRowNum()+1);
		System.out.println(abstract_ref_rownum+"-abstract_ref_rownum,"+abstract_ref_colnum+"-abstract_ref_rownum, "+ref_cell+"formula");
		msheet.getRow(abstract_ref_rownum).createCell(abstract_ref_colnum).setCellFormula(ref_cell);
	}
	
	protected void setHyperlinkInMeasurementSheet(XSSFWorkbook workbook, String source, Cell target, ItemAbstractDataTo itemAbsData) {
		XSSFSheet msheet = workbook.getSheet(Worksheets.MEASUREMENTSHEET);
		Cell c_description = null;
		CellReference ref_source = new CellReference(source);
		 Row r = msheet.getRow(ref_source.getRow());
		 if (r != null) {
		    c_description = r.getCell(r.getFirstCellNum());
		 }
		 System.out.println("description cell: "+ExcelUtill.getCellReference(c_description));
		 System.out.println("source :"+source+", target : "+ExcelUtill.getCellReference(target));
		 System.out.println("set hyperlink in measurement sheet for cell : "+ref_source.getRow() +":"+ref_source.getCol()+", "+ExcelUtill.getCellReference(target));
		c_description.setCellFormula("HYPERLINK(CELL(\"address\","+Worksheets.ABSTRACTSHEET+"!"+ExcelUtill.getCellReference(target)+"), T_PAGE_NUMBER & "+MeasurementSheetConstants.MEASUREMENT_PAGE_NUMBER+")");
		
	}
	
	protected String getAmountFormula(int rownum, int fullRateColNum, int partRateColNum, int quantityColNum, Double quantityPerUnit, boolean isFinalBill){
		//=ROUND(D20*G20,2)
		String f1 = ExcelUtill.intToChar(quantityColNum) + "" + (rownum);
		String f2 = "";
		if(isFinalBill){
			f2 = ExcelUtill.intToChar(fullRateColNum) + "" + (rownum);
		}else{
			f2 = ExcelUtill.intToChar(partRateColNum) + "" + (rownum);
		}
		return "=ROUND(("+f1+"*"+f2+")/"+quantityPerUnit+",2)";
	}
	
	protected String getContractorsAbbatmentFormula(Clause clause, float percentg, int amountCol, int rowNum){
		String f1 = clause == Clause.Below ? "-" : "";
		return "="+f1+"ROUND("
				+ ExcelUtill.intToChar(amountCol) + (rowNum-1) + "*"
				+ percentg + "%,2)";
	}
	
	
	protected String getQuantityFormula(int firstRow, int lastRow, int quantityCol){
		String f1 = ExcelUtill.intToChar(quantityCol)+""+(firstRow+1);
		String f2 = ExcelUtill.intToChar(quantityCol)+""+(lastRow-1);
		String formula = "=SUM("+f1+":"+f2+")";
		return formula;
	}

	
	protected String getDescriptionFormula(int rowNum, int pageNumColNum, String msheetRef){
		char pageNumCol = (char) (pageNumColNum + 65);
		String description_formula = CellIdentifier.ABSTRACT_TOTAL_IDENTIFIER_FORMULA.replace(CellIdentifier.ABSTRACT_TOTAL_IDENTIFIER_FORMULA_PARAM, pageNumCol+""+(rowNum));
		String description = "=HYPERLINK(CELL(\"address\","+msheetRef+"),"+description_formula+")";
		return description;
	}
	
	protected String getPreviousItemAbstractDescription(String previousSno){
		//"Quantity BF from <<>> CMB No. "
		String s1 = "Quantity BF from ";
		s1+=previousSno;
		s1+=" CMB No. ";
		return s1;
	}
	
	protected int getMetaDataColumnNumber(Workbook wb) {
		// retrieve the named range
		int namedCellIdx = wb
				.getNameIndex(MeasurementSheetConstants.MEASUREMENT_TITLES_RANGE_NAME);
		Name aNamedCell = wb.getNameAt(namedCellIdx);

		// retrieve the cell at the named range and test its contents
		AreaReference aref = new AreaReference(aNamedCell.getRefersToFormula());
		CellReference[] crefs = aref.getAllReferencedCells();
		System.out.println("namedcell index :" + crefs);
		for (int i = 0; i < crefs.length; i++) {
			System.out.println(crefs[i].getSheetName());
			System.out.println(crefs[i].getRow());
			System.out.println(i);
			String title = wb.getSheet(Worksheets.MEASUREMENTSHEET)
					.getRow(crefs[i].getRow()).getCell(i).getStringCellValue();
			System.out.println(title);
			if (MeasurementSheetConstants.MEASUREMENT_TITLES_METADATA_NAME
					.equals(title)) {
				return i;
			}
		}
		return 0;
	}


}
