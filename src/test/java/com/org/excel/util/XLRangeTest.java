package com.org.excel.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream.GetField;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

public class XLRangeTest extends XLTest{
	

	//@Test
	public void test() throws IOException {
		init();
		XLColumnRange xlrange = new XLColumnRange(workbook, "CHECK");
		System.out.println(xlrange.getFirstColNum()+","+xlrange.getLastColNum());
	}

}
