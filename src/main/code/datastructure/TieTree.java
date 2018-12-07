package main.code.datastructure;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import main.code.parse.Record;

public class TieTree {
	
	private TrieNode rootNode = new SingleCharNode();

	
	public TieTree() {
		
	}
	
	public void addStrings(Map<String, Record> records) {		
		for(Entry<String, Record> entry:records.entrySet()) {			
			rootNode.insert(entry.getKey().toCharArray(),0,entry.getValue());	

		}
	}

	
	public List<String> getAllStrings(char[] prefix) {
		List<String> returnList = new ArrayList<String>();
		rootNode.findStringsWithPrefix(prefix,0, new String(), returnList);			
		return returnList;
	}
	
	public void writeRecord(char[] chars, DataInputStream dataIn) throws IOException {
		rootNode.write(chars,0, dataIn);
	}
	
}

