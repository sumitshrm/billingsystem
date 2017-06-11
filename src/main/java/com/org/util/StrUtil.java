package com.org.util;

public class StrUtil {
	
	public static String toHTMLUtill(String input){
		return input.replaceAll("(\r\n|\n)", "<br />");
	}
	
	public static int extractNumbers(String input){
		if(input==null){
			return -1;
		}
		String numberOnly= input.replaceAll("[^0-9]", "");
		return Integer.parseInt(numberOnly);
	}

}
