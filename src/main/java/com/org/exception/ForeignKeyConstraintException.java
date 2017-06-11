package com.org.exception;

public class ForeignKeyConstraintException extends Exception{
	public ForeignKeyConstraintException(String type, String id) {
		super(type +"'"+ id + "' can not be removed.");
	}
}
