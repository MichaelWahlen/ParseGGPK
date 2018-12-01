package main.code.parse;

import java.util.ArrayList;
import java.util.List;

public class Record {
	
	private String tag = "Not set";
	private String name = "Not set";
	private long startMarker = 0;
	private long length = 0;
	private int numberOfEntries = 0;
	private List<Long> references = new ArrayList<Long>();
	private long totalMoved = 0;
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getStartMarker() {
		return startMarker;
	}
	public void setStartMarker(long startMarker) {
		this.startMarker = startMarker;
	}
	
	public long getLength() {
		return length;
	}
	
	public void setLength(long length) {
		this.length = length;
	}
	
	public int getNumberOfEntries() {
		return numberOfEntries;
	}
	
	public void setNumberOfEntries(int numberOfEntries) {
		this.numberOfEntries = numberOfEntries;
	}
	
	public List<Long> getReferences() {
		return references;
	}
	
	public void addReference(Long reference) {
		references.add(reference);
	}
	
	public void printToConsole() {
		System.out.println("Tag: "+getTag());
		System.out.println("StartMarker: "+getStartMarker());
		System.out.println("Next start marker:"+(getTotalMoved()+getStartMarker()));
		System.out.println("Expected byte size: "+getLength());	
		System.out.println("Actual processed byte size: "+getTotalMoved());	
		System.out.println("Expected number of references: "+getNumberOfEntries());	
		System.out.println("Actual found references: "+references.size());			
		System.out.println("File or folder name: "+getName());	
		System.out.println("\n");
	}
	
	public void validate() throws RecordValidationException {
		if(getNumberOfEntries()!=0&&getNumberOfEntries()!=getReferences().size()) {
			throw new RecordValidationException("Actual reference count not equal to expected count.");
		}
		if(getLength()!=0&&getLength()!=getTotalMoved()) {
			throw new RecordValidationException("Actual record size not equal to expected size.");
		}
	}
	
	public long getTotalMoved() {
		return totalMoved;
	}
	public void setTotalMoved(long totalMoved) {
		this.totalMoved = totalMoved;
	}
}
