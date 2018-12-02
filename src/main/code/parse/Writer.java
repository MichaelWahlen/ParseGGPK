package main.code.parse;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class Writer {
	
	private DataInputStream readStream;
	private Map<Long, Record> recordMap;
	
	public Writer(DataInputStream dataIn, Map<Long, Record> map) throws IOException {
		this.setReadStream(dataIn);
		this.setRecordMap(map);
		writeFiles();
	}

	private void writeFiles() {
		DataInputStream stream =  getReadStream();
		Map<Long, Record> records = getRecordMap();
	
		for(Record record:records.values()) {
			try{switch(record.getTag()) {
			case "FILE":
				stream.skip(record.getHeaderSize());
				File file;
				if(record.getAbsoluteTargetFilePath()==null) {
					file = new File("C:\\ggpkextract\\ROOT",record.getName().replaceAll("[^A-Za-z0-9.]", ""));
				}
				else {
					file = new File(record.getAbsoluteTargetFilePath());
				}
				file.createNewFile();
				byte[] bytesRead = new byte[(int) (record.getLength()-record.getHeaderSize())];		
				stream.readFully(bytesRead);
				try (FileOutputStream fos = new FileOutputStream(file)) {
					fos.write(bytesRead);					  
				}
				break;
			default:
				stream.skip(record.getLength());
			}
			} catch(Exception e) {
				record.printToConsole();
			}
		}
			
		
		
	}



	public Map<Long, Record> getRecordMap() {
		return recordMap;
	}

	public void setRecordMap(Map<Long, Record> recordMap) {
		this.recordMap = recordMap;
	}

	public DataInputStream getReadStream() {
		return readStream;
	}

	public void setReadStream(DataInputStream readStream) {
		this.readStream = readStream;
	}
	
}
