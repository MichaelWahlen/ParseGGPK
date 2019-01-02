package main.code.parse;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.brotli.dec.BrotliInputStream;
import main.code.record.Payload;
import main.code.record.PayloadType;
import main.code.record.Record;

public class BinaryPayloadProcessor {
	
	private File sourceFile;
	
	public BinaryPayloadProcessor(String absoluteSourcePath) throws ValidationException {
		File file = new File(absoluteSourcePath);		
		if(file.exists()&&file.canRead()) {
			setSourceFile(file);
		} else {
			throw new ValidationException("File at "+absoluteSourcePath+" cannot be opened.");
		}
	}

	private void writeDDS(Payload payload, Long startBit, DataInputStream dataIn) throws IOException {				
		File targetFile = new File(payload.getTargetPath());
		targetFile.getParentFile().mkdirs();		
		targetFile.createNewFile();				
		if(payload !=null && payload.getPayloadType() == PayloadType.DDS) {				
			dataIn.skip(startBit);
			byte[] fullPayLoad = new byte[payload.getLength()];		
			dataIn.readFully(fullPayLoad);	
			FileOutputStream outputStream = new FileOutputStream(targetFile);
			switch(payload.getCompressionType()) {
				case NOT_COMPRESSED:
					outputStream.write(ByteBuffer.wrap(fullPayLoad).order(payload.getByteOrder()).array());							
					break;
				case REFERENCE:
					System.out.println("Found reference in DDS file: "+ new String(ByteBuffer.wrap(fullPayLoad).order(payload.getByteOrder()).array(),"UTF-8"));
					break;
				case BROTLI:
					byte[] decompressedPayload = new byte[payload.getExpectedDecompressedLength()];			
					BrotliInputStream brotliStream = new BrotliInputStream(new ByteArrayInputStream(ByteBuffer.wrap(fullPayLoad).order(payload.getByteOrder()).array()));
					brotliStream.read(decompressedPayload);			
					brotliStream.close();					
					outputStream.write(decompressedPayload);	
					break;
				default:									
			}
			outputStream.close();				
		}		
	}
	
	public void write(List<Record> records) throws IOException {
		Collections.sort(records, new Comparator<Record>() {
		    @Override
		    public int compare(Record o1, Record o2) {
		        return ((Long) o1.getStartBit()).compareTo(o2.getStartBit());
		    }
		});
		DataInputStream dataIn = new DataInputStream(new FileInputStream(getSourceFile()));
		long startBitModifier = 0L;
		for(Record record: records) {
			Payload payload = record.getPayload();	
			if(payload!=null&& payload.getPayloadType() == PayloadType.DDS) {
				writeDDS(payload, payload.getStartBit()-startBitModifier,dataIn);
				startBitModifier = payload.getStartBit() + payload.getLength();
			}
		}
		dataIn.close();	
	}
	
	private File getSourceFile() {
		return sourceFile;
	}

	private void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}
	
	public void changeAbsoluteSourcePath(String sourcePath) {
		
	}
	
	
}
