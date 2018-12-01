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
	
	private long bufferLocation = 0;
	private Map<Long, Record> records = new HashMap<Long, Record>();
	Set<Long> rootReferences = new HashSet<Long>();
	
	private long getBufferLocation() {
		return bufferLocation;
	}
	
	private void progressMarkerLocation(long readBytes) {
		bufferLocation =  bufferLocation + readBytes;
	}
	
	private void skipBytes(DataInputStream stream, long numberOfBytesToRead) throws IOException {
		stream.skip(numberOfBytesToRead);
		progressMarkerLocation(numberOfBytesToRead);
	}
	
	private ByteBuffer readBytes(DataInputStream stream, int numberOfBytesToRead) throws IOException {
		byte[] bytesRead = new byte[numberOfBytesToRead];
		progressMarkerLocation(numberOfBytesToRead);
		stream.readFully(bytesRead);
		return ByteBuffer.wrap(bytesRead).order(ByteOrder.LITTLE_ENDIAN);
	}
	
	private void addToRecords(Record record) {
		records.put(record.getStartMarker(), record);
		record.printToConsole();
		try {
			record.validate();
		} catch (RecordValidationException e) {
			System.out.println(e.getMessage());		
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void processGGPK(DataInputStream dataIn, long startMarker, long length) throws IOException {
		long totalMoved = 8;
		Record record = new Record();
		record.setTag("GGPK");
		record.setStartMarker(startMarker-totalMoved);
		record.setLength(length);		
   	 	int numberOfRecords = readBytes(dataIn,4).getInt();
   	 	totalMoved = totalMoved + 4;
   	 	record.setNumberOfEntries(numberOfRecords);		
   	 	for(int i = 0;i<numberOfRecords;i++) {
	   		long rootReference = readBytes(dataIn,8).getLong();	
	   		totalMoved = totalMoved +8;
	   		record.addReference(rootReference);	   		
   		}
   	 	record.setTotalMoved(totalMoved);
   	 	addToRecords(record);
	}
	
	
	private void processFREE(DataInputStream dataIn, long startMarker, long length) throws IOException {
		long totalMoved = 8;
		Record record = new Record();
		record.setTag("FREE");
		record.setStartMarker(startMarker-totalMoved);
		record.setLength(length);		
		skipBytes(dataIn,length-8);
		totalMoved = totalMoved + length-8;
    	record.setTotalMoved(totalMoved);
    	addToRecords(record);
	}
	
	private void processFILE(DataInputStream dataIn, long startMarker, long length) throws IOException {
		long totalMoved = 8;
		Record record = new Record();
		record.setTag("FILE");
		record.setStartMarker(startMarker-totalMoved);
		record.setLength(length);		
		int lengthOfName = readBytes(dataIn,4).getInt();	
		totalMoved = totalMoved + 4;
		skipBytes(dataIn,32);	
		totalMoved = totalMoved + 32;
		record.setName(new String(readBytes(dataIn,2*(lengthOfName-1)).array(), "UTF-8"));
		totalMoved = totalMoved + 2*(lengthOfName-1);
		// skip payload: length designated at start of record - tag length - length of the length - length of the name *2 - null terminator
		skipBytes(dataIn,length-8-2*lengthOfName-32-2);		
		totalMoved = totalMoved + length-8-2*lengthOfName-32-2;
		record.setTotalMoved(totalMoved);
		addToRecords(record);
	}
	
	private void processPDIR(DataInputStream dataIn, long startMarker, long length) throws IOException {
		long totalMoved = 8;
		Record record = new Record();
		record.setTag("PDIR");
		record.setStartMarker(startMarker-totalMoved);
		record.setLength(length);		
		int lengthOfName = readBytes(dataIn,4).getInt();
		totalMoved = totalMoved +4;
		int totalEntryInDir = readBytes(dataIn,4).getInt();	
		totalMoved = totalMoved +4;
		record.setNumberOfEntries(totalEntryInDir);
		 // Seems to 32 bytes of hash that are placed in the file
		 skipBytes(dataIn,32);	
		 totalMoved = totalMoved +32;
		 // times 2 becauses of useless space everywhere except at the end and start?				
		 record.setName(new String(readBytes(dataIn,2*(lengthOfName-1)).array(), "UTF-8"));	
		 totalMoved = totalMoved + 2*(lengthOfName-1);
		 //System.out.println("Name = "+name);
		 // skipping null terminator
		 skipBytes(dataIn,2);	
		 totalMoved = totalMoved +2;
		 for(int i = 0 ; i < totalEntryInDir;i++) {
			 readBytes(dataIn,4);	
			 totalMoved = totalMoved + 4;
			 long directoryEntry = readBytes(dataIn,8).getLong();
			 totalMoved = totalMoved + 8;
			 record.addReference(directoryEntry);
		 }
		 record.setTotalMoved(totalMoved);
    	 addToRecords(record);
	}
	
	
	public void parseGGPK(String pathToFile) {
		DataInputStream dataIn = null;
		Map<Long,String> locations = new HashMap<Long,String>();
		Set<Long> references = new HashSet<Long>();		
		String elementName = "ROOT_DIR_9999";
		try {
			 dataIn = new DataInputStream(new FileInputStream(pathToFile));
			 while(dataIn.available()>0) {				
				 locations.put(getBufferLocation(),elementName);							 
		         int length = readBytes(dataIn,4).getInt();	
		         //System.out.println(length);
		         //System.out.println(Integer.toUnsignedLong(length));
		         String tag = new String(readBytes(dataIn,4).array(), "UTF-8");		       	         
		         switch(tag) {
		         case "GGPK": 
		        	 processGGPK(dataIn,getBufferLocation(),length);		        	 
		        	 break;
		         case "FREE":
		        	 processFREE(dataIn,getBufferLocation(),length);
		        	 break;
		         case "FILE":	 
		        	 processFILE(dataIn,getBufferLocation(),length);
					 break;
		         case "PDIR":
		        	 processPDIR(dataIn,getBufferLocation(),length);
					 break;
				 default:
					System.out.println("Unknown tag");
					System.exit(0);
		         }
		      }
		} catch (IOException e) {			
			e.printStackTrace();
		} finally {
			try {
				dataIn.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	
	
	public static void main(String[] args) {
		String ggpkAbsolutePath = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk";
		ParseGGPK parse = new ParseGGPK();
		parse.parseGGPK(ggpkAbsolutePath);
	}
	
}
