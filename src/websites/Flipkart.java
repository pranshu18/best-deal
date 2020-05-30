package websites;

public class Flipkart extends Website{

	public Flipkart() {
		 searchBarXpath="//form[@action='/search']//input[@type='text']";
		 submitButtonXpath="//form[@action='/search']//button[@type='submit']";
		 itemsListXpath="//div[@class='_1HmYoV _35HD7C']/div[@class='bhgxx2 col-12-12']/div/div[@data-id]";
		 itemNamesXpath="./div/a[2]";
		 itemRatingsXpath="./div/div[1]/span[1]/div";	
		 itemNumberOfReviewsXpath="./div/div[1]/span[2]";
		 itemLinksXpath="./div/a[2]";
		 itemOutOfStockXpath="./div/a[1]/div[2]/span";
		 itemPricesXpath="./div/a[3]/div/div[1]";
		 itemDiscountsXpath="./div/a[3]/div/div[3]/span";
		 
		 seller="Flipkart";
		 currency="Rs";
		 url="https://www.flipkart.com/";
	}

	
}
