package com.org.entity;

public class AbstractTo {
	private Item item;
	private double quantity;
	
	public AbstractTo(Item item, double quantity) {
		super();
		this.item = item;
		this.quantity = quantity;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
}
