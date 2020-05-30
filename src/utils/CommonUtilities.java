package utils;

import java.util.ArrayList;
import java.util.List;

public class CommonUtilities {

	public static String getMonthDigitStringFromWord(String monthWord) throws IllegalArgumentException{
		String mm="";
		
		switch(monthWord) {
		case "Jan":
		case "January":
			mm="01";
			break;
		case "Feb":
		case "February":
			mm="02";
			break;
		case "Mar":
		case "March":
			mm="03";
			break;
		case "Apr":
		case "April":
			mm="04";
			break;
		case "May":
			mm="05";
			break;
		case "Jun":
		case "June":
			mm="06";
			break;
		case "Jul":
		case "July":
			mm="07";
			break;
		case "Aug":
		case "August":
			mm="08";
			break;
		case "Sep":
		case "September":
			mm="09";
			break;
		case "Oct":
		case "October":
			mm="10";
			break;
		case "Nov":
		case "November":
			mm="11";
			break;
		case "Dec":
		case "December":
			mm="12";
			break;
		default:
			throw new IllegalArgumentException("Month "+monthWord+" not recognized, please add.");	
		}
		return mm;
	}

	//KMP algorithm implementation
	public static boolean substringExists(String text, String pattern) {

		boolean found=false;

		List<Integer> positions=findPattern(pattern,text);

		if(!positions.isEmpty())
			found=true;

		return found;
	}

	public static List<Integer> findPattern(String pattern, String text) {
		ArrayList<Integer> result = new ArrayList<Integer>();

		String s=pattern+"$"+text;
		int[] prefix=computePrefix(s);

		for(int i=0;i<s.length();i++) {
			if(i>pattern.length() && prefix[i]==pattern.length()) {
				result.add(i-(2*pattern.length()));
			}
		}

		return result;
	}

	private static int[] computePrefix(String text) {

		int[] prefix=new int[text.length()];

		prefix[0]=0;
		int currentBorder=0;

		for(int i=1;i<text.length();i++) {
			while(currentBorder>0 && text.charAt(i)!=text.charAt(currentBorder)) {
				currentBorder=prefix[currentBorder-1];
			}
			if(text.charAt(i)==text.charAt(currentBorder))
				currentBorder +=1;
			else
				currentBorder=0;
			prefix[i]=currentBorder;
		}
		return prefix;
	}


	public static String removeNonNumericCharacters(String nonNumericString) {
		String numericString="";
		
		int i=0;
		while(i<nonNumericString.length()) {
			char c=nonNumericString.charAt(i);
			switch(c) {
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '0':
				numericString +=c;
				break;
			}
			i++;
		}
		return numericString;
	}

	public static int getEditDistance(String s, String t) {
		int lenS=s.length();
		int lenT=t.length();
		
		int distances[][]=new int[lenS+1][lenT+1];
		
		for(int i=0;i<=lenS;i++) {
			for(int j=0;j<=lenT;j++) {
				if(i==0)
					distances[i][j]=j;
				else if(j==0)
					distances[i][j]=i;
				else if(s.charAt(i-1)==t.charAt(j-1))
					distances[i][j]=distances[i-1][j-1];
				else {
					distances[i][j]=1+Math.min(distances[i-1][j-1], Math.min(distances[i][j-1], distances[i-1][j]));
				}
			}
		}
		
		
		return distances[lenS][lenT];
	}

}
