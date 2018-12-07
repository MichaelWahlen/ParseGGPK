package main.code.datastructure;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import main.code.parse.Record;

public interface TrieNode {

	public void insert(char[] chars, int position, Record record);
	public boolean isStringEnd();
	public void markAsStringEnd();
	public void findStringsWithPrefix(char[] prefix, int position, String currentString, List<String> holderList);
	public void setRecord(Record record);
	public Record getRecord();
	public void write(char[] chars, int position, DataInputStream dataIn) throws IOException;

	
}

