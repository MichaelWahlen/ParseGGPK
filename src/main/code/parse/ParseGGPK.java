package main.code.parse;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParseGGPK {	

	private long markerLocation = 0;
	private Map<Long, Record> records = new HashMap<Long, Record>();
	private boolean hasRecordConsolePrinting = false;
	private boolean usesRecordValidation = true;	
	private String absolutePathToBinaryFile;
	private Set<Long> foundReferences = new HashSet<Long>();
	private Set<Long> foundFileStartMarkers = new HashSet<Long>();
	private Set<Long> missingReferences = new HashSet<Long>();
	
	public ParseGGPK(String fileLocation) {
		this.absolutePathToBinaryFile = fileLocation;
	}
	
	public Map<Long, Record> getRecords(){
		return records;
	}
	
	private String getAbsolutePathToBinaryFile() {
		return absolutePathToBinaryFile;
	}
	
	private long getMarkerLocation() {
		return markerLocation;
	}
		
	/**
	 * Increases the location of the position marker in the binary file, by the amount of processed bytes. <br>
	 * Increases the total of read bytes as recorded with the record object. <br>
	 * @param record the record that contains the extracted information for the current header + content lines in the binary file
	 * @param movedPositions the number of moved positions
	 */
	private void progressMarkerLocation(Record record,long movedPositions) {
		markerLocation =  markerLocation + movedPositions;
		record.increaseMovement(movedPositions);
	}
	
	/**
	 * Skips n bytes and records the location after skipping, of the position marker in the binary file.
	 * @param record the record that contains the extracted information for the current header + content lines in the binary file
	 * @param stream stream that wraps the binary file that is being read
	 * @param numberOfBytesToRead the number of bytes that should be skipped
	 * @throws IOException thrown if the data stream cannot be accessed
	 */
	private void skipBytes(Record record,DataInputStream stream, long numberOfBytesToRead) throws IOException {
		stream.skip(numberOfBytesToRead);
		progressMarkerLocation(record,numberOfBytesToRead);
	}
	
	/**
	 * Reads n bytes, changes the Endian ordering to LITTLE_ENDIAN and records the location after reading, of the position marker in the binary file.
	 * @param record the record that contains the extracted information for the current header + content lines in the binary file
	 * @param stream stream that wraps the binary file that is being read
	 * @param numberOfBytesToRead the number of bytes that should be read
	 * @return return the read binary data in the form of a ByteBuffer
	 * @throws IOException thrown if the data stream cannot be accessed
	 */
	private ByteBuffer readBytes(Record record,DataInputStream stream, int numberOfBytesToRead) throws IOException {
		progressMarkerLocation(record,numberOfBytesToRead);
		byte[] bytesRead = new byte[numberOfBytesToRead];		
		stream.readFully(bytesRead);
		return ByteBuffer.wrap(bytesRead).order(ByteOrder.LITTLE_ENDIAN);
	}
	
	/**
	 * Adds a record to the set of identified records. Currently also holds the option of calling both a print and a verify.
	 * @param record the record that needs to be added to the set of identified records in the parsed binary file.
	 */
	private void addToRecords(Record record) {
		records.put(record.getStartMarker(), record);
		if(hasRecordConsolePrinting) {
			record.printToConsole();			
		}
		if(usesRecordValidation) {
			try {
				record.validate();
			} catch (ValidationException e) {
				System.out.println(e.getMessage());		
				e.printStackTrace();
				System.exit(0);
			}
		}
	}
	
	/**
	 * GGPK record is the record at the start of the file that contain the reference to the root directory, and to a single FREE record. <br>
	 * GGPK record type content consists of: <br>
	 * 4 bytes that indicated the number of contained reference. By definition this should always be equal to 2. <br>
	 * x*8 bytes where x is the amount of references (by definition 2) and the 8 bytes contain the reference to a PDIR record or a FREE record. <br>
	 * @param record the custom record class used to contain record related information
	 * @param dataIn the data stream that contains the data
	 * @throws IOException thrown if file cannot be accessed
	 */	
	private void processGGPK(Record record,DataInputStream dataIn) throws IOException {
   	 	int numberOfRecords = readBytes(record,dataIn,4).getInt();
   	 	record.setNumberOfEntries(numberOfRecords);	
   	 	record.lockHeaderSize();
   	 	for(int i = 0;i<numberOfRecords;i++) {
	   		long rootReference = readBytes(record,dataIn,8).getLong();
	   		record.addReference(rootReference);	 
	   	
   		}
	}
	
	/**
	 * FREE records are records that used to hold data and are now deleted and "free". <br>
	 * FREE record type content consists of: <br>
	 * x-8 bytes, where x is the length defined in the header, and 8 is the amount of bytes in the header.
	 * @param record the custom record class used to contain record related information
	 * @param dataIn the data stream that contains the data
	 * @throws IOException thrown if file cannot be accessed
	 */	
	private void processFREE(Record record,DataInputStream dataIn) throws IOException {		
		record.lockHeaderSize();
		skipBytes(record,dataIn,record.getLength()-8);
		
	}
	
	/**
	 * FILE records contain information regarding the actual files that used by the game <br>
	 * FILE record type content consists of: <br>
	 * 4 bytes containing the length of the file name. <br>
	 * 32 bytes containing a hash of something. <br>
	 * 2*(n-1) bytes (depending on length n of file name) containing the name of the file. Reason for the additional length is use of spaces in between characters. <br>
	 * 2 bytes for a null terminator (separator). <br>
	 * x-8-32-2-2*(n-1)-4 bytes for the payload. This is calculated as the expected length of the record minus the bytes used for meta data (i.e. hash, separator, etc)
	 * @param record the custom record class used to contain record related information
	 * @param dataIn the data stream that contains the data
	 * @throws IOException thrown if file cannot be accessed
	 */	
	private void processFILE(Record record,DataInputStream dataIn) throws IOException {			
		int lengthOfName = readBytes(record,dataIn,4).getInt();		
		skipBytes(record,dataIn,32);		
		record.setName(new String(readBytes(record,dataIn,2*(lengthOfName-1)).array(), "UTF-8"));	
		record.lockHeaderSize();
		skipBytes(record,dataIn,record.getLength()-8-2*lengthOfName-32-2);
		if(usesRecordValidation) {
			foundFileStartMarkers.add(record.getStartMarker());
   		}
	}
	
	/**
	 * PDIR records contain information regarding the (physical) directory structure <br>
	 * PDIR record type content consists of: <br>
	 * 4 bytes containing the length of directory name. <br>
	 * 4 bytes containing the number of expected entries in this directory. <br>
	 * 32 bytes containing a hash of something. <br>
	 * 2*(n-1) bytes (depending on length n of directory name) containing the name of the directory. Reason for the additional length is use of spaces in between characters. <br>
	 * 2 bytes for a null terminator (separator). <br>
	 * (4+8)*x bytes, where there is 4 bytes for a hash, followed by 8 bytes for a reference to the location of the content of the directory. <br>
	 * @param record the custom record class used to contain record related information
	 * @param dataIn the data stream that contains the data
	 * @throws IOException thrown if file cannot be accessed
	 */	
	private void processPDIR(Record record,DataInputStream dataIn) throws IOException {		
		int lengthOfName = readBytes(record,dataIn,4).getInt();
		int totalEntryInDir = readBytes(record,dataIn,4).getInt();
		record.setNumberOfEntries(totalEntryInDir);
		skipBytes(record,dataIn,32);
		record.setName(new String(readBytes(record,dataIn,2*(lengthOfName-1)).array(), "UTF-8"));
		skipBytes(record,dataIn,2);
		record.lockHeaderSize();
		for(int i = 0 ; i < totalEntryInDir;i++) {
			skipBytes(record,dataIn,4);
			long reference = readBytes(record,dataIn,8).getLong();
			record.addReference(reference);
			if(usesRecordValidation) {
	   			foundReferences.add(reference);
	   		}
		}
    	
	}
	
	
	/**
	 * Binary file is big endian (java expects little endian). This means that bit ordering needs to be swapped.<br>
	 * File structure consists of a repetition of headers followed by content<br>
	 * Every header and content will be captured in a custom Record object<br>
	 * Header consists of 4 bytes containing expected length of the header + content.<br>
	 * Followed by 4 bytes containing the tag defining the actual type of the record, assumed to be in UTF-8.<br>
	 * Followed by a variable set of actual content. <br>
	 * @param pathToFile absolute path to the GGPK file
	 */	
	public void parseGGPK() {
		DataInputStream dataIn = null;
		try {
			 dataIn = new DataInputStream(new FileInputStream(getAbsolutePathToBinaryFile()));
			 while(dataIn.available()>0) {
				Record record = new Record();			 	
			 	record.setStartMarker(getMarkerLocation());			 	
		        record.setLength(readBytes(record,dataIn,4).getInt());
		        record.setTag(new String(readBytes(record,dataIn,4).array(), "UTF-8"));		        
		 	    switch(record.getTag()) {
		        case "GGPK": 
			       	processGGPK(record,dataIn);		        	 
			        break;
		        case "FREE":
			        processFREE(record,dataIn);
			        break;
		        case "FILE":	 
			        processFILE(record,dataIn);
					break;
		        case "PDIR":
			        processPDIR(record,dataIn);
					break;
				default:
					System.out.println("Unknown tag");
					System.exit(0);
		        }
		 	   addToRecords(record);
		      }
		} catch (IOException e) {			
			e.printStackTrace();
		} finally {
			try {
				dataIn.close();
				if(usesRecordValidation) {
					Set<Long> missingReferences = getMissingReferences();
					for(long reference:foundFileStartMarkers) {
						if(!foundReferences.contains(reference)) {
							missingReferences.add(reference);
						}
					}
				}
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}		
	}
	
	public static void main(String[] args) throws ValidationException, IOException {	
		ParseGGPK parse = new ParseGGPK("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk");		
		parse.parseGGPK();
		DirectoryStructure structure = new DirectoryStructure(parse.getRecords());
		structure.buildDirectoryStructure("C:\\ggpkextract");
		DataInputStream dataIn = new DataInputStream(new FileInputStream("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk"));
		Writer writer = new Writer(dataIn,parse.getRecords());
	}

	private Set<Long> getMissingReferences() {
		return missingReferences;
	}

	
}
