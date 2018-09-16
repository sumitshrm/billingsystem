package com.org.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillformsTo {

	private Aggreement aggreement;
	
	List<String> selectedReports;
	
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

	

	
}
