package com.org.exception;

public class ParentItemNotFoundException extends Exception{

	public ParentItemNotFoundException(String itemNum) {
		super("item number '"+itemNum + "' not found");
		
	}
}
