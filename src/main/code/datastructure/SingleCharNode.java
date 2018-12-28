package main.code.datastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class allows for a simple Tie Tree construction where Strings are deconstructed into their CHAR representation and then 
 * @author LUPOTJ1
 * @param <T> the type of object that might be contained in a key marked node
 */
public class SingleCharNode<T> implements TrieNode<T> {
	
	private Map<Character, TrieNode<T>> childNodes = new HashMap<Character, TrieNode<T>>();
	private boolean keyMarker = false;
	private T leafObject = null;	
	
	@Override
	public void insertKey(char[] characters, int charactersProcessed, T object) {
		char currentCharacter = characters[charactersProcessed];
		TrieNode<T> childNode = childNodes.get(currentCharacter);		
		if (childNode==null) {
			childNode = new SingleCharNode<T>();
			childNodes.put(currentCharacter, childNode);
		}		
		if(charactersProcessed==characters.length-1) {
			childNode.setEndMarker(true);
			childNode.setObject(object);
		} else {
			charactersProcessed++;
			childNode.insertKey(characters,charactersProcessed,object);
		}		
	}
	
	@Override
	public void insertKey(char[] characters, int position) {
		insertKey(characters,position, null);
	}

	@Override
	public void findPrefixedKeys(String prefix, int prefixPositionsChecked, List<String> prefixedKeys) {		
		if(prefixPositionsChecked+1<=prefix.length()) {
			TrieNode<T> foundChild = childNodes.get(prefix.charAt(prefixPositionsChecked));
			if(foundChild!=null) {				
				if (prefixPositionsChecked==prefix.length()-1&&foundChild.hasEndMarker()){					
					prefixedKeys.add(prefix);					
				}
				foundChild.findPrefixedKeys(prefix, prefixPositionsChecked+1,prefixedKeys);
			}			
		} else {
			findStringsAfterPrefix(prefix,prefixedKeys);
		}		
	}
	
	@Override
	public void findStringsAfterPrefix(String currentString, List<String> holderList) {
		for(Entry<Character, TrieNode<T>> entry:childNodes.entrySet()) {
			String tempString = new String(currentString) + entry.getKey();
			TrieNode<T> foundChild = entry.getValue();
			if(foundChild.hasEndMarker()) {
				holderList.add(tempString);
			}
			foundChild.findStringsAfterPrefix(tempString,holderList);
		}
	}
	
	@Override
	public T findObject(char[] key, int position) {		
		T returnObject = null;
		char currentCharacter = key[position];
		TrieNode<T> childNode = childNodes.get(currentCharacter);	
		if (childNode!=null) {
			if(position==key.length-1&&childNode.hasEndMarker()) {
				returnObject = childNode.getObject();
			} else if (position<key.length-1){
				position++;
				returnObject = childNode.findObject(key,position);
			}	
		} 		
		return returnObject;
	}

	@Override
	public T getObject() {
		return leafObject;		
	}
	
	@Override
	public void setObject(T leafObject) {
		this.leafObject = leafObject;		
	}

	@Override
	public void setEndMarker(boolean keyMarker) {
		this.keyMarker = keyMarker;		
	}
	
	@Override
	public boolean hasEndMarker() {		
		return keyMarker;
	}
	
}
