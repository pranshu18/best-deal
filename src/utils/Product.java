package utils;

import java.util.ArrayList;

public class Product {

	protected String productName="";
	protected ArrayList<Item> items=new ArrayList<Item>();
	
	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public Item getItemAt(int index) {
		return this.items.get(index);
	}
	
	public void addItem(Item item) {
		if(this.items==null)
			this.items=new ArrayList<Item>();
		this.items.add(item);
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}
