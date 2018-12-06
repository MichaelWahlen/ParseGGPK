package main.code.utilities;

import java.util.ArrayList;
import java.util.List;

public class StringUtilities { 
	
	private char delimiter;
	
	public StringUtilities(char delimiter) {
		this.delimiter = delimiter;
	}
	
	public static String provideCapital(String string) {
		return Character.toUpperCase(string.charAt(0)) + string.substring(1);	
	}
	
	public List<String> decomposeString(String sourceString){
		List<String> row = new ArrayList<String>();
		char[] searchArray = sourceString.toCharArray();
		int startPosition = 0;
		for (int i =0; i < searchArray.length;i++) {
			if(searchArray[i]==delimiter) {								
				row.add(sourceString.substring(startPosition,i));			
				startPosition = i + 1;			
			}
		}
		row.add(sourceString.substring(startPosition).replace("\"", ""));	
		return row;
	} 
	
	public String composeString(List<String> strings){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(strings.get(0));		
		for(int i = 1;i<strings.size();i++) {
			stringBuilder.append(delimiter+strings.get(i));
		}		
		return stringBuilder.toString();
	} 

}
