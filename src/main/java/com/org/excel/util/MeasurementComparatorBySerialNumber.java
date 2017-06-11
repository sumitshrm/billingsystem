package com.org.excel.util;

import java.util.Comparator;

import com.org.entity.MeasurementSheet;

public class MeasurementComparatorBySerialNumber implements Comparator<MeasurementSheet> {

	@Override
	public int compare(MeasurementSheet o1, MeasurementSheet o2) {
		if(o1.getSerialNumber()>o2.getSerialNumber())
			return 1;
		if(o1.getSerialNumber()==o2.getSerialNumber())
			return 0;
		else
			return -1;
	}

}
