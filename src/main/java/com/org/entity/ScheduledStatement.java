package com.org.entity;

import java.util.List;

public class ScheduledStatement {

	String nameOfWork;
	
	List<EstimateItem> items;
	
	String test;

	public String getNameOfWork() {
		return nameOfWork;
	}

	public void setNameOfWork(String nameOfWork) {
		this.nameOfWork = nameOfWork;
	}

	public List<EstimateItem> getItems() {
		return items;
	}

	public void setItems(List<EstimateItem> items) {
		this.items = items;
	}

	
	
	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	@Override
	public String toString() {
		return "ScheduledStatement [nameOfWork=" + nameOfWork + ", items=" + items + "]";
	}
	
	

	
}
