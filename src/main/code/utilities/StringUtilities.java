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
	
	public static String findExtension(String sourceString, char extensionPrefix) {
		char[] sourceChars = sourceString.toCharArray();
		char[] targetChars = null;		
		for(int i = sourceChars.length-1;i>=0;i--) {
			if(sourceChars[i]==extensionPrefix) {
				targetChars = new char[sourceChars.length-i];
				System.arraycopy(sourceChars, i, targetChars, 0, sourceChars.length-i);	
				break;
			}
		}		
		if(targetChars==null) {
			targetChars = new char[4];
			targetChars[0] = 'n';
			targetChars[1] = 'u';
			targetChars[2] = 'l';
			targetChars[3] = 'l';
		}
		
		return new String(targetChars);		
	}
	
	//as I do not want to make a JUNIT for this, for now a main to test :)
	public static void main(String[] args) {
		System.out.println(findExtension("bla.txt",'.'));
		System.out.println(findExtension("blatxt",'.'));
		System.out.println(findExtension("b.l.a.txt",'.'));
	}

}
