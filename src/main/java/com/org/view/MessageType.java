package com.org.view;

public enum MessageType {
	ERROR, INFO, SUCCESS, WARNING;
	
	public String toString(){
		return this.toString().toLowerCase();
	}
}
