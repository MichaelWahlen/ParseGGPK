package main.code.datastructure;

import java.util.ArrayList;
import java.util.List;

public class TieTree {
	
	private TrieNode rootNode = new SingleCharNode();

	
	public TieTree() {
		
	}
	
	public void addStrings(List<String> strings) {		
		for(String string:strings) {			
			rootNode.insertChars(string.toCharArray(),0);		
		}
	}

	public boolean findPrefix(char[] prefix) {		
		boolean prefixFound = rootNode.findPrefix(prefix,0);
		return prefixFound;
	}
	
	public List<String> getAllStrings(char[] prefix) {
		List<String> returnList = new ArrayList<String>();
		rootNode.findStringsWithPrefix(prefix,0, new String(), returnList);			
		return returnList;
	}
	
}

