package main.code.datastructure;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.code.parse.Record;

public class SingleCharNode implements TrieNode {
	
	private Map<Character, TrieNode> childNodes = new HashMap<Character, TrieNode>();
	private boolean isStringEnd = false;	
	private Record record;
	
	public SingleCharNode() {
		
	}
	
	@Override
	public void setRecord(Record record) {
		this.record = record;		
	}
	
	@Override
	public Record getRecord() {
		return record;
	}
	
	@Override
	public void insert(char[] chars, int position, Record record) {
		char currentChar = chars[position];
		TrieNode childNode = childNodes.get(currentChar);
		if (childNode!=null) {
			if(position+1==chars.length) {
				childNode.markAsStringEnd();
				childNode.setRecord(record);
			} else {
				childNode.insert(chars,position+1,record);
			}
		} else {
			childNode = new SingleCharNode();
			childNodes.put(currentChar, childNode);
			if(position+1==chars.length) {
				childNode.markAsStringEnd();
				childNode.setRecord(record);
			} else {
				childNode.insert(chars,position+1,record);
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
				if(node.getValue().isStringEnd()) {
					String localString = currentString + node.getKey();
					holderList.add(localString);
				}
				node.getValue().findStringsWithPrefix(prefix, position+1,currentString + node.getKey(),holderList);
			}
		}		
	}

	@Override
	public void write(char[] chars, int position, DataInputStream dataIn) throws IOException {
		if(chars.length==position) {		
			if(isStringEnd) {
				record.write(dataIn);
			}
		} else {
			TrieNode child = childNodes.get(chars[position]);
			if(child!=null) {
				child.write(chars,position+1,dataIn);
			} 
		}
	}




}