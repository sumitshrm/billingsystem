package com.org.excel.gateway;

import java.util.List;

import com.org.entity.Item;

public class ItemsTo {
	
	private String itemsAsJSON;
	
	private boolean extraItem;

	private List<Item> items;
	
	private List<String> deletedItems;
	
	private Long msheetId;
	
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public List<String> getDeletedItems() {
		return deletedItems;
	}
	public void setDeletedItems(List<String> deletedItems) {
		this.deletedItems = deletedItems;
	}
	public String getItemsAsJSON() {
		return itemsAsJSON;
	}
	public void setItemsAsJSON(String itemsAsJSON) {
		this.itemsAsJSON = itemsAsJSON;
	}
	public boolean isExtraItem() {
		return extraItem;
	}
	public void setExtraItem(boolean extraItem) {
		this.extraItem = extraItem;
	}
	public Long getMsheetId() {
		return msheetId;
	}
	public void setMsheetId(Long msheetId) {
		this.msheetId = msheetId;
	}
	
}
