package main.code.datastructure;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TieTree<T> {
	
	private TrieNode<T> rootNode = new SingleCharNode<T>();

	
	public TieTree() {
		
	}
	
	public void addEntries(List<String> strings) {		
		for(String string:strings) {			
			if(string.length()>0) {
				rootNode.insertKey(string.toCharArray(),0);	
			}
		}
	}

	public void addEntries(Map<String,T> map) {		
		for(Entry<String,T> entry:map.entrySet()) {			
			if(entry.getKey().length()>0) {
				rootNode.insertKey(entry.getKey().toCharArray(),0,entry.getValue());	
			}
		}
	}
	
	public boolean findPrefix(String prefix) {		
		boolean isPrefixFound = true;
		if(getPrefixedKeys(prefix).size()==0) {
			isPrefixFound = false;
		}		
		return isPrefixFound;
	}
	
	public List<String> getPrefixedKeys(String prefix) {
		List<String> returnList = new ArrayList<String>();
		rootNode.findPrefixedKeys(prefix,0, returnList);			
		return returnList;
	}
	
	public T getObject(String key) {
		return rootNode.findObject(key.toCharArray(), 0);		
	}
	
}
