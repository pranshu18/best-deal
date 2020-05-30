package websites;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import utils.CommonUtilities;
import utils.Item;
import utils.Product;


@SuppressWarnings("unchecked")
public class Website {

	 String searchBarXpath="";
	 String submitButtonXpath="";
	 String itemsListXpath="";
	 String itemNamesXpath="";
	 String itemRatingsXpath="";	
	 String itemNumberOfReviewsXpath="";
	 String itemLinksXpath="";
	 String itemOutOfStockXpath="";
	 String itemPricesXpath="";
	 String itemDiscountsXpath="";
	 String itemDeliveryDatesXpath="";

	 String seller="";
	 String currency="";
	 String url="";

	 HtmlPage currentPage;

	public void fillDetails(WebClient client, String query, Product product) throws FailingHttpStatusCodeException, MalformedURLException, IOException, URISyntaxException{

		boolean initialized=initializeSearch(client,query);

		if(initialized) {		
			List<HtmlElement> resultList=(List<HtmlElement>) (Object) currentPage.getByXPath(itemsListXpath);

			if(resultList.isEmpty()){  
				System.out.println("Nothing found on " + seller);
			}else{
				for(HtmlElement result:resultList) {
					try {
						extractInfo(query,result,product);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected void extractInfo(String query, HtmlElement result, Product product) throws URISyntaxException {
		List<HtmlElement> nameElements=(List<HtmlElement>)(Object)result.getByXPath(itemNamesXpath);

		if(nameElements.size()==1) {
			String itemName=nameElements.get(0).asText();
			boolean found=CommonUtilities.substringExists(itemName.toUpperCase(),query.toUpperCase());

			if(found) {
				Item item=new Item();

				item.setSeller(seller);
				item.setItemName(itemName);
				item.setCurrency(currency);
				System.out.print(itemName);
				
				extractRating(result,item);
				extractNumberOfReviews(result,item);
				extractLinks(result,item);
				extractOutOfStockInfo(result,item);
				extractPrices(result,item);
				extractDiscounts(result,item);
				extractDeliveryDate(result,item);
				
				product.addItem(item);

				System.out.println();

			}
		}
	}

	protected void extractDeliveryDate(HtmlElement result, Item item) {
		
	}

	protected void extractDiscounts(HtmlElement result, Item item) {
		List<HtmlElement> itemDiscounts = (List<HtmlElement>) (Object)result.getByXPath(itemDiscountsXpath);

		if(itemDiscounts.size()==1) {
			String discount=itemDiscounts.get(0).asText();
			discount=CommonUtilities.removeNonNumericCharacters(discount);
			float discountPercentage=Float.parseFloat(discount);
			item.setDiscountPercentage(discountPercentage);
			System.out.print("\t"+discountPercentage);
		}
	}

	protected  void extractPrices(HtmlElement result, Item item) {
		List<HtmlElement> itemPrices = (List<HtmlElement>) (Object)result.getByXPath(itemPricesXpath);

		if(itemPrices.size()==1) {
			String priceString=itemPrices.get(0).asText();
			priceString=CommonUtilities.removeNonNumericCharacters(priceString);
			float price=Float.parseFloat(priceString);
			item.setPrice(price);
			System.out.print("\t"+price);
		}		
	}

	protected void extractOutOfStockInfo(HtmlElement result, Item item) {
		List<HtmlElement> itemOutOfStock = (List<HtmlElement>) (Object) result.getByXPath(itemOutOfStockXpath);

		if(itemOutOfStock.size()==1) {
			item.setOutOfStock(true);
		}
	}

	protected void extractLinks(HtmlElement result, Item item) throws URISyntaxException {
		List<HtmlElement> itemLinks = (List<HtmlElement>) (Object) result.getByXPath(itemLinksXpath);

		if(itemLinks.size()==1) {
			String href=itemLinks.get(0).getAttribute("href");
			String link="";
			if(!href.startsWith("http"))
				link=url.substring(0, url.length()-1)+href;
			else
				link=href;
			URI uri = new URI(link);
			item.setLink(uri);

			System.out.print("\t"+link);
		}
	}

	protected void extractNumberOfReviews(HtmlElement result, Item item) {
		List<HtmlElement> itemNumberOfReviews = (List<HtmlElement>) (Object) result.getByXPath(itemNumberOfReviewsXpath);

		if(itemNumberOfReviews.size()==1) {
			String numberOfReviews=itemNumberOfReviews.get(0).asText();
			numberOfReviews=CommonUtilities.removeNonNumericCharacters(numberOfReviews);
			int number=	Integer.parseInt(numberOfReviews);
			item.setNumberOfReviews(number);
			System.out.print("\t"+number);
		}
	}

	protected void extractRating(HtmlElement result, Item item) {
		List<HtmlElement> itemRatings = (List<HtmlElement>) (Object) result.getByXPath(itemRatingsXpath);

		if(itemRatings.size()==1) {
			String itemRating=itemRatings.get(0).asText();
			float rating=Float.parseFloat(itemRating.trim());
			item.setRating(rating);
			System.out.print("\t"+rating);
		}
	}

	protected boolean initializeSearch(WebClient client, String query) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		boolean successful=false;
		
		currentPage = client.getPage(url);

		List<HtmlTextInput> searchBars=null;

		searchBars=(List<HtmlTextInput>)(Object) currentPage.getByXPath(searchBarXpath);

		if(searchBars.isEmpty()) {
			System.err.println("Please check xpath of "+seller+" search bar");
		}else {
			searchBars.get(0).setText(query);
			List<HtmlElement> submitButtons=(List<HtmlElement>) (Object) currentPage.getByXPath(submitButtonXpath);
			currentPage=submitButtons.get(0).click();
			successful=true;
		}
		return successful;
	}
}
