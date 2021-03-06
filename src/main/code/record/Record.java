package main.code.record;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import main.code.parse.ValidationException;

/**
 * Class holds all relevant meta information on the contained record in the GGPK file. <br>
 * The actual payload (if any) is not contained in the record, as parsing the GGPK file would result in too much memory being occupied otherwise. <br>
 * No actual processing takes place within the class, it functions as a container only.
 * @author MichaelWahlen  
 */

public class Record {
	
	private RecordType recordType = RecordType.NOT_DEFINED;
	private String name = "Not set";
	private long startMarker = 0;
	private long length = 0;
	private int numberOfEntries = 0;
	private List<Long> references = new ArrayList<Long>();
	private long totalMoved = 0;
	private long headerSize = 0;
	private boolean hasDefinedFilePath = false;
	private Payload payload = new Payload();

	private BigInteger hash;
	
	public RecordType getRecordType() {
		return recordType;
	}
	public void setRecordType(RecordType recordType) {
		this.recordType = recordType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getStartBit() {
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
		System.out.println("Record tag: "+getRecordType());
		System.out.println("Record start bit: "+getStartBit());		
		System.out.println("Record length: "+getLength());
		System.out.println("Record header length: "+getHeaderSize());
		System.out.println("Record expected entries: "+getNumberOfEntries());				
		System.out.println("Record name: "+getName());	
		System.out.println("Record target path: "+getAbsoluteTargetFilePath());
		System.out.println("Record hash: "+getHash());		
		payload.printToConsole();
		System.out.println("---------------------------");
	}
	
	public void validate() throws ValidationException {
		if(getNumberOfEntries()!=0&&getNumberOfEntries()!=getReferences().size()) {
			throw new ValidationException("Actual reference count not equal to expected count.");
		}
		if(getLength()!=0&&getLength()!=getTotalMoved()) {
			throw new ValidationException("Actual record size not equal to expected size.");
		}
	}
	
	private long getTotalMoved() {
		return totalMoved;
	}
	
	public void increaseMovement(long readBytes) {
		this.totalMoved = this.totalMoved + readBytes;
	}
	
	public String getAbsoluteTargetFilePath() {
		return payload.getTargetPath();
	}
	
	public void setAbsoluteTargetFilePath(String absoluteTargetFilePath) {
		payload.setTargetPath(absoluteTargetFilePath);
		hasDefinedFilePath = true;
	}
	
	public boolean hasPathSet() {
		return hasDefinedFilePath;
	}
	
	public long getHeaderSize() {
		return headerSize;
	}

	public void lockHeaderSize() {
		headerSize= totalMoved;
	}

	public BigInteger getHash() {
		return hash;
	}
	
	public void setHash(BigInteger hash) {
		this.hash = hash;
	}
	
	public Payload getPayload() {
		return payload;
	}
	
	public void setPayload(Payload payload) {
		this.payload = payload;
	}
	
}
