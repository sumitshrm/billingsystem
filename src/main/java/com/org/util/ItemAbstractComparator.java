package com.org.util;

import java.util.Comparator;

import com.org.entity.ItemAbstract;

public class ItemAbstractComparator implements Comparator<ItemAbstract>{

	@Override
	public int compare(ItemAbstract o1, ItemAbstract o2) {
		if(o1.getItem().getSortOrder()>o2.getItem().getSortOrder())
			return 1;
		else if(o1.getItem().getSortOrder()==o2.getItem().getSortOrder())
			return 0;
		else
			return -1;
	}

}
