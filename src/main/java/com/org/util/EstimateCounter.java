package com.org.util;

public class EstimateCounter {
	
	private int msheetCounter;
	
	private int abstractCounter;

	public EstimateCounter(int msheetCounter, int abstractCounter) {
		super();
		this.msheetCounter = msheetCounter;
		this.abstractCounter = abstractCounter;
	}

	public int getMsheetCounter() {
		return msheetCounter;
	}

	public void setMsheetCounter(int msheetCounter) {
		this.msheetCounter = msheetCounter;
	}

	public int getAbstractCounter() {
		return abstractCounter;
	}

	public void setAbstractCounter(int abstractCounter) {
		this.abstractCounter = abstractCounter;
	}
	
	public int nextMsheetCounter() {
		this.msheetCounter++;
		return msheetCounter;
	}
	
	public int nextAbstractCounter() {
		this.abstractCounter++;
		return abstractCounter;
		
	}

}
