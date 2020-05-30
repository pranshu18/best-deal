package menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utils.Item;
import utils.Product;

public class ExcelActionListener implements ActionListener {

	String query="";
	Product product;
	String orderByType="";
	JFrame f;
	
	public ExcelActionListener(String query, Product product, String orderByType, JFrame f) {
		this.query=query;
		this.product=product;
		this.orderByType=orderByType;
		this.f=f;
	}

	private void saveToExcel(File selectedDirectory) {
		XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("By "+orderByType);

    	Row row= sheet.createRow(0);
    	row.createCell(0).setCellValue("Name");
    	row.createCell(1).setCellValue("Seller");
    	row.createCell(2).setCellValue("Out of Stock");
    	row.createCell(3).setCellValue("Price");
    	row.createCell(4).setCellValue("Number of Reviews");
    	row.createCell(5).setCellValue("Rating");
    	row.createCell(6).setCellValue("Discount Percentage");
    	row.createCell(7).setCellValue("Link");

    	int i=1;
        for(Item item:product.getItems()) {
        	row= sheet.createRow(i);
        	
        	row.createCell(0).setCellValue(item.getItemName());
        	row.createCell(1).setCellValue(item.getSeller());
        	
    		if (!item.isOutOfStock()) 
    			row.createCell(2).setCellValue("NO");
    		else
    			row.createCell(2).setCellValue("YES");

        	row.createCell(3).setCellValue(item.getPrice());
        	row.createCell(4).setCellValue(item.getNumberOfReviews());
        	row.createCell(5).setCellValue(item.getRating());
        	row.createCell(6).setCellValue(item.getDiscountPercentage());
        	row.createCell(7).setCellValue(item.getLink().toString());
        	i++;
        }
		
    	FileOutputStream fos; 		
		try {
			String currentTime=LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss"));
			fos = new FileOutputStream(selectedDirectory+File.separator+query+" results_"+currentTime+".xlsx");
			workbook.write(fos);
			fos.close();
			workbook.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser folderSelector = new JFileChooser(); 
		folderSelector.setCurrentDirectory(new File(System.getProperty("user.dir")));
		folderSelector.setDialogTitle("Choose directory in which to save excel report");
		folderSelector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		folderSelector.setAcceptAllFileFilterUsed(false);
		
		f.add(folderSelector);
		
		if (folderSelector.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			saveToExcel(folderSelector.getSelectedFile());
	}

}
