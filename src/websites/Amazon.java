package websites;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;

import utils.CommonUtilities;
import utils.Item;

@SuppressWarnings("unchecked")
public class Amazon extends Website{

	public Amazon() {
		itemsListXpath="//div[contains(@class,'s-result-list s-search-results sg-row')]/div";
		itemNamesXpath=".//h2/a/span";
		itemRatingsXpath=".//div[contains(@class,'a-row a-size-small')]/span[1]";	
		itemNumberOfReviewsXpath=".//div[contains(@class,'a-row a-size-small')]/span[2]";
		itemPricesXpath=".//span[contains(@class,'a-price-whole')]";
		itemDiscountsXpath=".//span[contains(@class,'a-letter-space')]/following-sibling::span[1]";
		itemDeliveryDatesXpath=".//div[contains(@class,'a-row s-align-children-center')]/span[2]/span[2]";
		itemLinksXpath=".//h2/a";

		url="https://www.amazon.in/";

		seller="Amazon";
		currency="Rs";
	}

	@Override
	protected void extractDiscounts(HtmlElement result, Item item) {
		List<HtmlElement> itemDiscounts = (List<HtmlElement>) (Object) result.getByXPath(itemDiscountsXpath);

		if(itemDiscounts.size()==1) {
			String discount=itemDiscounts.get(0).asText();
			int bracketIndex=discount.indexOf("(");
			if(bracketIndex!=-1) {
				discount=discount.substring(bracketIndex+1, discount.length()-2);
				float discountPercentage=Float.parseFloat(discount);
				item.setDiscountPercentage(discountPercentage);
				System.out.print("\t"+discountPercentage);
			}
		}
	}

	@Override
	protected boolean initializeSearch(WebClient client, String query) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String searchUrl=url+"s?k="+URLEncoder.encode(query, "UTF-8")+"&ref=nb_sb_noss_2";

		currentPage = client.getPage(searchUrl);

		return true;
	}

	@Override
	protected void extractRating(HtmlElement result, Item item) {
		List<HtmlElement> itemRatings = (List<HtmlElement>) (Object) result.getByXPath(itemRatingsXpath);

		if(itemRatings.size()==1) {
			String itemRating=itemRatings.get(0).getAttribute("aria-label");
			int spaceIndex=itemRating.indexOf(" ");
			if(spaceIndex!=-1) {
				float rating=Float.parseFloat(itemRating.substring(0, spaceIndex));
				item.setRating(rating);
				System.out.print("\t"+rating);
			}
		}
	}

	@Override
	protected void extractOutOfStockInfo(HtmlElement result, Item item) {

	}

	@Override
	protected void extractPrices(HtmlElement result, Item item) {
		List<HtmlElement> itemPrices = (List<HtmlElement>) (Object) result.getByXPath(itemPricesXpath);

		if(itemPrices.size()==1) {
			String priceString=itemPrices.get(0).asText();
			priceString=CommonUtilities.removeNonNumericCharacters(priceString);
			float price=Float.parseFloat(priceString);
			item.setPrice(price);
			System.out.print("\t"+price);
		}else if(itemPrices.size()==0) {
			item.setOutOfStock(true);
		}
	}

	@Override
	protected void extractDeliveryDate(HtmlElement result, Item item) {
		List<HtmlElement> itemDeliveryDates = (List<HtmlElement>) (Object) result.getByXPath(itemDeliveryDatesXpath);

		if(itemDeliveryDates.size()==1) {
			String dateString=itemDeliveryDates.get(0).asText();
			LocalDate date=getDateFromString(dateString);
			item.setDeliveryDate(date);

			System.out.print("\t"+date);

		}
	}

	private LocalDate getDateFromString(String dateString) throws IllegalArgumentException{

		String dd="",mm="",yyyy="";
		String[] words=dateString.split(" ");
		LocalDate date=null;
		if(words.length==3 && words[0].endsWith(",")) {

			mm=CommonUtilities.getMonthDigitStringFromWord(words[1]);

			dd=words[2];

			if(dd.length()<2) {
				dd="0"+dd;
			}			
			yyyy=getPossibleYear(mm);

			String formattedDate=dd+mm+yyyy;

			date=LocalDate.parse(formattedDate, DateTimeFormatter.ofPattern("ddMMyyyy"));
		}

		return date;
	}

	private String getPossibleYear(String monthDigits) {

		String year="";
		LocalDate today=LocalDate.now();

		String currentYear=today.format(DateTimeFormatter.ofPattern("yyyy"));
		String currentMonthDigits=today.format(DateTimeFormatter.ofPattern("MM"));

		if(monthDigits.equals("01") && currentMonthDigits.equals("12")) {
			int yearInt=Integer.parseInt(currentYear)+1;
			year=Integer.toString(yearInt);
		}else {
			year=currentYear;
		}
		return year;
	}

}
