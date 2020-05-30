package menu;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import scraping.Search;
import utils.Item;
import utils.Product;


public class MainMenu implements ActionListener{

	public final static String PRICE="Price";
	public final static String DELIVERY_DATE="Delivery Date";
	public final static String NUMBER_OF_REVIEWS="Number of Reviews";
	public final static String RATING="Rating";
	public final static String DISCOUNT_PERCENTAGE="Discount Percentage";

	public final static int FIXED_COMPONENTS=6;

	String[] orderByList=new String[] {PRICE,NUMBER_OF_REVIEWS,RATING,DISCOUNT_PERCENTAGE};

	JFrame f=new JFrame("Best Deals!");		
	JLabel label1=new JLabel("Please enter the item you wish to purchase");
	JTextField t1=new JTextField("");  
	JButton b1=new JButton("Submit");
	JButton b2=new JButton("Save results to excel");
	JLabel label2=new JLabel("Please select how to rank results");
	JComboBox<String> comboBox = new JComboBox<String>(orderByList);
	JCheckBox checkBox = new JCheckBox("Display out of stock results?",false);

	String prevQuery="";
	String prevOrderType="";
	boolean prevCheckboxVal;
	Product product=null;

	public void launchMenu() {
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

		label1.setBounds(515, 100, 300, 30);
		t1.setBounds(540,150, 200,30);  
		label2.setBounds(540, 200, 300, 30);
		comboBox.setBounds(400, 250, 200, 30);
		checkBox.setBounds(630, 250, 300, 30);
		b1.setBounds(540,300, 200, 30);

		f.setLayout(null);
		f.setSize(1500, 1000);
		f.add(label1);
		f.add(b1);
		f.add(t1);
		f.add(label2);
		f.add(comboBox);
		f.add(checkBox);
		f.setVisible(true);

		b1.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String orderByType=(String) comboBox.getSelectedItem();

		if(!prevQuery.equalsIgnoreCase(t1.getText())) {
			product=Search.getResults(t1.getText(),orderByType);
			prevOrderType=orderByType;
			prevQuery=t1.getText();
			prevCheckboxVal=checkBox.isSelected();
			displayResults();
		}else if(!orderByType.equalsIgnoreCase(prevOrderType)) {
			prevOrderType=orderByType;
			prevCheckboxVal=checkBox.isSelected();

			Search.orderResults(orderByType, product);

			displayResults();
		}else if(prevCheckboxVal!=checkBox.isSelected()) {
			prevCheckboxVal=checkBox.isSelected();
			displayResults();
		}
	}

	private void displayResults() {
		Component[] componentList = f.getContentPane().getComponents();

		int i=0;
		for(Component c : componentList){
			if(i>=FIXED_COMPONENTS){
				f.remove(c);
			}
			i++;
		}
		f.revalidate();
		f.repaint();

		addHeaders();

		int y=400;
		i=0;
		for(Item item:product.getItems()) {
			if(!checkBox.isSelected() && item.isOutOfStock())
				continue;
			addItemData(item,y);
			y +=50;
			if(i==5)
				break;
			i++;
		}
		
		addExcelReportButton();
		
		f.revalidate();
		f.repaint();				
	}

	
	private void addExcelReportButton() {
		b2.setBounds(750,300, 200, 30);

		b2.addActionListener(new ExcelActionListener(t1.getText(),product, (String) comboBox.getSelectedItem(), f));
		f.add(b2);
	}

	private void addItemData(Item item, int yCoordinate) {
		JLabel nameLabel,priceLabel,reviewsLabel,ratingLabel,discountLabel,outOfStockLabel;
		nameLabel=new JLabel(item.getItemName());
		nameLabel.setToolTipText(item.getItemName());
		if (!item.isOutOfStock()) {
			outOfStockLabel=new JLabel("NO");
		}else
			outOfStockLabel=new JLabel("YES");

		float price=item.getPrice();
		if(price==Float.MAX_VALUE)
			priceLabel = new JLabel("NA");
		else
			priceLabel = new JLabel(Float.toString(item.getPrice()));
		reviewsLabel = new JLabel(Integer.toString(item.getNumberOfReviews()));
		ratingLabel = new JLabel(Float.toString(item.getRating()));
		discountLabel = new JLabel(Float.toString(item.getDiscountPercentage()) + "%");


		JButton itemLink=new JButton("Go to site");

		nameLabel.setBounds(40, yCoordinate+50, 200, 30);
		outOfStockLabel.setBounds(270, yCoordinate+50, 200, 30);
		priceLabel.setBounds(500, yCoordinate+50, 200, 30);
		reviewsLabel.setBounds(730, yCoordinate+50, 200, 30);
		ratingLabel.setBounds(960, yCoordinate+50, 100, 30);
		discountLabel.setBounds(1090, yCoordinate+50, 200, 30);

		itemLink.setBounds(1320,yCoordinate+50,100,30);

		URI uri=item.getLink();

		itemLink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Desktop d=Desktop.getDesktop();
				try {
					d.browse(uri);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		f.add(nameLabel);
		f.add(outOfStockLabel);
		f.add(priceLabel);
		f.add(reviewsLabel);
		f.add(ratingLabel);
		f.add(discountLabel);
		f.add(itemLink);
	}

	private void addHeaders() {
		JLabel dealsLabel=new JLabel("The best deals are -");
		dealsLabel.setBounds(40, 350, 200, 30);
		f.add(dealsLabel);

		JLabel nameLabel=new JLabel("Name");
		nameLabel.setBounds(40, 400, 200, 30);
		f.add(nameLabel);

		JLabel outOfStockLabel=new JLabel("Out of Stock");
		outOfStockLabel.setBounds(270, 400, 200, 30);
		f.add(outOfStockLabel);

		JLabel priceLabel=new JLabel(MainMenu.PRICE);
		priceLabel.setBounds(500, 400, 200, 30);
		f.add(priceLabel);

		JLabel reviewsLabel=new JLabel(MainMenu.NUMBER_OF_REVIEWS);
		reviewsLabel.setBounds(730, 400, 200, 30);
		f.add(reviewsLabel);

		JLabel ratingLabel=new JLabel(MainMenu.RATING);
		ratingLabel.setBounds(960, 400, 100, 30);
		f.add(ratingLabel);

		JLabel discountLabel=new JLabel(MainMenu.DISCOUNT_PERCENTAGE);
		discountLabel.setBounds(1090, 400, 200, 30);
		f.add(discountLabel);

		JLabel linkLabel=new JLabel("Link");
		linkLabel.setBounds(1320, 400, 100, 30);
		f.add(linkLabel);		
	}
}
