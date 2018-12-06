package main.code.datastructure;

import java.util.List;

public interface TrieNode {

	public void insertChars(char[] chars, int position);
	public boolean isStringEnd();
	public void markAsStringEnd();
	public boolean findPrefix(char[] prefix, int i);
	public void findStringsWithPrefix(char[] prefix, int position, String currentString, List<String> holderList);

	
}

