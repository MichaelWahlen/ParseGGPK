package main.code.datastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.code.parse.Record;

public class SingleCharNode implements TrieNode {
	
	private Map<Character, TrieNode> childNodes = new HashMap<Character, TrieNode>();
	private boolean isStringEnd = false;
	// referencing the item in the trie might be nice :)
	private Record record;
	
	public SingleCharNode() {
		
	}

	@Override
	public void insertChars(char[] chars, int position) {
		char currentChar = chars[position];
		TrieNode childNode = childNodes.get(currentChar);
		if (childNode!=null) {
			if(position+1==chars.length) {
				childNode.markAsStringEnd();
			} else {
				childNode.insertChars(chars,position+1);
			}
		} else {
			childNode = new SingleCharNode();
			childNodes.put(currentChar, childNode);
			if(position+1==chars.length) {
				childNode.markAsStringEnd();
			} else {
				childNode.insertChars(chars,position+1);
			}
		}
	}

	@Override
	public boolean isStringEnd() {		
		return isStringEnd;
	}
	
	@Override
	public void markAsStringEnd() {
		isStringEnd = true;
	}

	@Override
	public boolean findPrefix(char[] prefix, int position) {
		char currentChar = prefix[position];
		boolean prefixFound = false;
		if(position+1!=prefix.length) {
			TrieNode foundChild = childNodes.get(currentChar);
			if(foundChild==null||foundChild.isStringEnd()) {				
				prefixFound = false;
			} else {
				prefixFound = foundChild.findPrefix(prefix, position+1);
			}
		} else if (position+1==prefix.length) {
			TrieNode foundChild = childNodes.get(currentChar);
			if(foundChild==null) {
				prefixFound = false;
			} else {
				prefixFound = true;
			}
		}		
		return prefixFound;
	}

	@Override
	public void findStringsWithPrefix(char[] prefix, int position, String currentString, List<String> holderList) {		
		if(prefix.length>position+1) {
			TrieNode foundChild = childNodes.get(prefix[position]);
			if(foundChild!=null) {			
				currentString = currentString + prefix[position];
				foundChild.findStringsWithPrefix(prefix, position+1,currentString,holderList);
			}
		} else if (prefix.length==position+1){
			TrieNode foundChild = childNodes.get(prefix[position]);
			if(foundChild!=null) {					
				currentString = currentString + prefix[position];
				if(foundChild.isStringEnd()) {
					holderList.add(currentString);
				}
				foundChild.findStringsWithPrefix(prefix, position+1,currentString,holderList);
			}
		} else {
			for(Entry<Character, TrieNode> node:childNodes.entrySet()) {
				currentString = currentString + node.getKey();
				if(node.getValue().isStringEnd()) {
					holderList.add(currentString);
				}
				node.getValue().findStringsWithPrefix(prefix, position+1,currentString,holderList);
			}
		}		
	}


}