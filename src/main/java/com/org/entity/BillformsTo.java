package com.org.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillformsTo {

	private Aggreement aggreement;
	
	List<String> selectedReports;
	
	private Integer serialNumber;
	
	private Integer totalAmount;
	
	private Integer uptoPreviousBill;
	
	private String signature1;

	private String signature2;
	
	private String signature3;
	
	private String signature4;
	
	public static String BF_SNO_OF_BILL = "BF_SNO_OF_BILL";
	public static String BF_TOTAL_AMOUNT = "BF_TOTAL_AMOUNT";
	public static String BF_PREV_AMOUNT = "BF_PREV_AMOUNT";
	public static String BF_SINCE_PREV_AMOUNT = "BF_SINCE_PREV_AMOUNT";
	public static String BF_SIGN_1 = "BF_SIGN_1";
	public static String BF_SIGN_2 = "BF_SIGN_2";
	public static String BF_SIGN_3 = "BF_SIGN_3";
	public static String BF_SIGN_4 = "BF_SIGN_4";
	public static String BF_AMOUNT_IN_WORDS = "BF_AMOUNT_IN_WORDS";
	
	
	
	private List<String> reports = new ArrayList<String>();
	
	public BillformsTo() {
		reports.add("CERT01");
		reports.add("REVIEW");
		reports.add("SCRUTINY");
		reports.add("RUNNING BILL");
		reports.add("FIRST AND FINAL");
		reports.add("RECOVERY");
		reports.add("TEST");
		reports.add("CHECKLIST");
		reports.add("MAND TEST PERFORMA");
		reports.add("FLR");
	}

	public Aggreement getAggreement() {
		return aggreement;
	}

	public void setAggreement(Aggreement aggreement) {
		this.aggreement = aggreement;
	}

	public List<String> getSelectedReports() {
		return selectedReports;
	}

	public void setSelectedReports(List<String> selectedReports) {
		this.selectedReports = selectedReports;
	}

	public List<String> getReports() {
		return reports;
	}

	public void setReports(List<String> reports) {
		this.reports = reports;
	}

	public Integer getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(Integer serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Integer getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getUptoPreviousBill() {
		return uptoPreviousBill;
	}

	public void setUptoPreviousBill(Integer uptoPreviousBill) {
		this.uptoPreviousBill = uptoPreviousBill;
	}

	public String getSignature1() {
		return signature1;
	}

	public void setSignature1(String signature1) {
		this.signature1 = signature1;
	}

	public String getSignature2() {
		return signature2;
	}

	public void setSignature2(String signature2) {
		this.signature2 = signature2;
	}

	public String getSignature3() {
		return signature3;
	}

	public void setSignature3(String signature3) {
		this.signature3 = signature3;
	}

	public String getSignature4() {
		return signature4;
	}

	public void setSignature4(String signature4) {
		this.signature4 = signature4;
	}


	

	
}
