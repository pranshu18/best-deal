package utils;

import java.net.URI;
import java.time.LocalDate;
import java.util.Comparator;

public class Item{

	protected String itemName="";
	protected float price=Float.MAX_VALUE;
	protected String seller="";
	protected boolean outOfStock=false;
	protected LocalDate deliveryDate=LocalDate.MAX;
	protected int numberOfReviews=0;
	protected float rating=0;
	protected float discountPercentage=0;
	protected String currency="";
	protected URI link=null;
	
	public URI getLink() {
		return link;
	}
	public void setLink(URI link) {
		this.link = link;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String name) {
		this.itemName = name;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		if(price<=0)
			throw new IllegalArgumentException("Invalid price, cannot be equal to or less than 0.");
		this.price = price;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
	public boolean isOutOfStock() {
		return outOfStock;
	}
	public void setOutOfStock(boolean goingOutOfStock) {
		this.outOfStock = goingOutOfStock;
	}
	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(LocalDate deliveryDate) throws IllegalArgumentException{
		if(deliveryDate.isBefore(LocalDate.now()))
			throw new IllegalArgumentException("Invalid delivery date, must be after today.");
		this.deliveryDate = deliveryDate;
	}
	public int getNumberOfReviews() {
		return numberOfReviews;
	}
	public void setNumberOfReviews(int numberOfReviews) {
		this.numberOfReviews = numberOfReviews;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) throws IllegalArgumentException{
		if(rating<0 || rating>5)
			throw new IllegalArgumentException("Invalid rating value, must be between 0 and 5.");
		this.rating = rating;
	}
	public float getDiscountPercentage() {
		return discountPercentage;
	}
	public void setDiscountPercentage(float discountPercentage) throws IllegalArgumentException{
		if(discountPercentage<0 || discountPercentage>100)
			throw new IllegalArgumentException("Invalid discount percentage, must be between 0 and 100.");
		this.discountPercentage = discountPercentage;
	}
	
	public class SortByDiscountPercentage implements Comparator<Item> 
	{ 
	    public int compare(Item a, Item b) 
	    { 
	    	if(a.discountPercentage>b.discountPercentage)
	    		return -1;
	    	else if(a.discountPercentage<b.discountPercentage)
	    		return 1;
	    	else
	    		return 0;
	    } 
	} 

	public class SortByRating implements Comparator<Item> 
	{ 
	    public int compare(Item a, Item b) 
	    { 
	    	if(a.rating>b.rating)
	    		return -1;
	    	else if(a.rating<b.rating)
	    		return 1;
	    	else
	    		return 0;
	    } 
	} 

	public class SortByNumberOfReviews implements Comparator<Item> 
	{ 
	    public int compare(Item a, Item b) 
	    { 
	    	if(a.numberOfReviews>b.numberOfReviews)
	    		return -1;
	    	else if(a.numberOfReviews<b.numberOfReviews)
	    		return 1;
	    	else
	    		return 0;
	    } 
	} 

	public class SortByDeliveryDate implements Comparator<Item> 
	{ 
	    public int compare(Item a, Item b) 
	    { 
	    	if(a.deliveryDate.isAfter(b.deliveryDate))
	    		return 1;
	    	else if(a.deliveryDate.isBefore(b.deliveryDate))
	    		return -1;
	    	else
	    		return 0;
	    } 
	} 

	public class SortByPrice implements Comparator<Item> 
	{ 
	    public int compare(Item a, Item b) 
	    { 
	    	if(a.price>b.price)
	    		return 1;
	    	else if(a.price<b.price)
	    		return -1;
	    	else
	    		return 0;
	    } 
	} 
}
