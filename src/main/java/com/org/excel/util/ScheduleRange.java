package com.org.excel.util;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.org.constants.AbstractSheetConstants;

public class ScheduleRange {
	
	private static String FNFB_PREFIX = "FNFB_SCHEDULE";
	
	private static final String strNameOfWork = "NAME_OF_WORK";
	
	private static final String strAgency = "AGENCY";
	
	private static final String strAggreementNum = "AGGREEMENT_NUM";
	
	private static final String strDateOfStart = "DOS";
	
	private static final String strDateOfCompletionS ="DOC_S";
	
	private static final String strDateOfCompletionA = "DOC_A";
	
	private static final String strSnoOfBill = "SNO_OF_BILL";
	
	private static final String strClause = "CLAUSE";
	
	private static final String strDateOfAbstract = "DOA";
	
	private static final String strItemNum = "ITEM_NUM";
	
	private static final String strDescriptionOfItem = "DESC_OF_ITEMS";
	
	private static final String strQuantExecUptoDate = "QEUD";
	
	private static final String strUnit = "UNIT";
	
	private static final String strRate = "RATE";
	
	private static final String strPaymentOnActualMeas = "POTBOAM";
	
	private static final String strTotalAmount = "TOTAL_AMOUNT";
	
	private XLColumnRange rNameOfWOrk;
	
	private XLColumnRange rAgency;
	
	private XLColumnRange rAggreementNum;
	
	private XLColumnRange rDateOfStart;
	
	private XLColumnRange rDateOfCompletionS;
	
	private XLColumnRange rDateOfCompletionA;
	
	private XLColumnRange rSnoOfBill;
	
	private XLColumnRange rClause;
	
	private XLColumnRange rDateOfAbstract;
	
	private XLColumnRange rItemNum;
	
	private XLColumnRange rDescriptionOfItem;
	
	private XLColumnRange rQuantExecUptoDate;
	
	private XLColumnRange rUnit;
	
	private XLColumnRange rRate;
	
	private XLColumnRange rPaymentOnActualMeas;
	
	private XLColumnRange rTotalAmount;
	
	public ScheduleRange(XSSFWorkbook workbook) {
		super();
		this.rNameOfWOrk =  new XLColumnRange(workbook, withPrefix(strNameOfWork));
		this.rAgency =  new XLColumnRange(workbook, withPrefix(strAgency));
		this.rAggreementNum =  new XLColumnRange(workbook, withPrefix(strAggreementNum));
		this.rDateOfStart = new XLColumnRange(workbook, withPrefix(strDateOfStart));
		this.rDateOfCompletionS = new XLColumnRange(workbook, withPrefix(strDateOfCompletionS));
		this.rDateOfCompletionA = new XLColumnRange(workbook, withPrefix(strDateOfCompletionA));
		this.rSnoOfBill = new XLColumnRange(workbook, withPrefix(strSnoOfBill));
		this.rClause = new XLColumnRange(workbook, withPrefix(strClause));
		this.rDateOfAbstract = new XLColumnRange(workbook, withPrefix(strDateOfAbstract));
		this.rItemNum = new XLColumnRange(workbook, withPrefix(strItemNum));
		this.rDescriptionOfItem = new XLColumnRange(workbook, withPrefix(strDescriptionOfItem));
		this.rQuantExecUptoDate = new XLColumnRange(workbook, withPrefix(strQuantExecUptoDate));
		this.rUnit = new XLColumnRange(workbook, withPrefix(strUnit));
		this.rRate = new XLColumnRange(workbook, withPrefix(strRate));
		this.rPaymentOnActualMeas = new XLColumnRange(workbook, withPrefix(strPaymentOnActualMeas));
		this.rTotalAmount = new XLColumnRange(workbook, withPrefix(strTotalAmount));
	}

	protected String withPrefix(String input){
		return getPrefix()+"_"+input;
	}
	
	protected String getPrefix(){
		return FNFB_PREFIX;
	}

	public XLColumnRange getrAgency() {
		return rAgency;
	}

	public XLColumnRange getrNameOfWOrk() {
		return rNameOfWOrk;
	}

	public XLColumnRange getrAggreementNum() {
		return rAggreementNum;
	}

	public XLColumnRange getrDateOfStart() {
		return rDateOfStart;
	}

	public XLColumnRange getrDateOfCompletionS() {
		return rDateOfCompletionS;
	}

	public XLColumnRange getrDateOfCompletionA() {
		return rDateOfCompletionA;
	}

	public XLColumnRange getrSnoOfBill() {
		return rSnoOfBill;
	}

	public XLColumnRange getrClause() {
		return rClause;
	}

	public XLColumnRange getrDateOfAbstract() {
		return rDateOfAbstract;
	}

	public XLColumnRange getrItemNum() {
		return rItemNum;
	}

	public XLColumnRange getrDescriptionOfItem() {
		return rDescriptionOfItem;
	}

	public XLColumnRange getrQuantExecUptoDate() {
		return rQuantExecUptoDate;
	}

	public XLColumnRange getrUnit() {
		return rUnit;
	}

	public XLColumnRange getrRate() {
		return rRate;
	}

	public XLColumnRange getrPaymentOnActualMeas() {
		return rPaymentOnActualMeas;
	}

	public XLColumnRange getrTotalAmount() {
		return rTotalAmount;
	}

}
