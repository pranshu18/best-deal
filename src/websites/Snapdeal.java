package websites;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

import utils.Item;

@SuppressWarnings("unchecked")
public class Snapdeal extends Website {

	public Snapdeal() {
		searchBarXpath="//div[@class='col-xs-14 search-box-wrapper']/input[@type='text']";
		submitButtonXpath="//div[@class='col-xs-14 search-box-wrapper']/button";
		itemsListXpath="//section/div[contains(@class,'col-xs-6')]";
		itemNamesXpath="./div[contains(@class,'product-tuple-description')]/div[contains(@class,'product-desc-rating')]/a/p";
		itemRatingsXpath="./div[contains(@class,'product-tuple-description')]/div[contains(@class,'product-desc-rating')]/a/div[2]/div/div[2]";	
		itemNumberOfReviewsXpath="./div[contains(@class,'product-tuple-description')]/div[contains(@class,'product-desc-rating')]/a/div[2]/p";
		itemLinksXpath="./div[contains(@class,'product-tuple-description')]/div[contains(@class,'product-desc-rating')]/a";
		itemOutOfStockXpath="./div[contains(@class,'product-tuple-image')]/a[2]/span[contains(@class,'badge-soldout')]";
		itemPricesXpath="./div[contains(@class,'product-tuple-description')]/div[contains(@class,'product-desc-rating')]/a/div[1]/div[1]/span[@class='lfloat product-price']";
		itemDiscountsXpath="./div[contains(@class,'product-tuple-description')]/div[contains(@class,'product-desc-rating')]/a/div[1]/div[2]/span";

		seller="Snapdeal";
		currency="Rs";
		url="https://www.snapdeal.com/";
	}


	@Override
	protected boolean initializeSearch(WebClient client, String query) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String searchUrl=url+"search?keyword="+URLEncoder.encode(query, "UTF-8")+"&noOfResults=20&sort=rlvncy";
		
		currentPage = client.getPage(searchUrl);

		return true;
	}


	@Override
	protected void extractRating(HtmlElement result, Item item) {
		List<HtmlElement> itemRatings = (List<HtmlElement>) (Object) result.getByXPath(itemRatingsXpath);

		if(itemRatings.size()==1) {
			String itemRating=itemRatings.get(0).getAttribute("style");
			int beginIndex=itemRating.indexOf(":");
			if (beginIndex!=-1) {
				itemRating = itemRating.substring(beginIndex+1, itemRating.length() - 1);
				float rating = ((Float.parseFloat(itemRating.trim()))*5)/100;
				item.setRating(rating);
				System.out.print("\t" + rating);
			}
		}
	}

}
