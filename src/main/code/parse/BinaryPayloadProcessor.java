package main.code.parse;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.brotli.dec.BrotliInputStream;

import main.code.record.Payload;
import main.code.record.PayloadType;
import main.code.record.Record;
import main.code.record.RecordType;



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
	
	public void write(Record record) throws IOException {		
		if(record.getRecordType()==RecordType.FILE) {
			DataInputStream dataIn = new DataInputStream(new FileInputStream(getSourceFile()));
			dataIn.skip(record.getStartMarker()+record.getHeaderSize());
			File targetFile = new File(record.getAbsoluteTargetFilePath());
			targetFile.getParentFile().mkdirs();		
			targetFile.createNewFile();		
			Payload payload = record.getPayload();			
			if(payload !=null && payload.getPayloadType() == PayloadType.DDS) {				
				byte[] fullPayLoad = new byte[(int)(record.getLength()-record.getHeaderSize())];		
				dataIn.readFully(fullPayLoad);			
				byte[] brotliHeader = new byte[4];
				System.arraycopy(fullPayLoad, 0, brotliHeader, 0, 4);			
				byte[] shortHeader = new byte[1];
				System.arraycopy(fullPayLoad, 0, shortHeader, 0, 1);				
				if(new String(ByteBuffer.wrap(brotliHeader).order(ByteOrder.LITTLE_ENDIAN).array(),"UTF-8").equals("DDS ")) {
					byte[] payLoad = new byte[(int)(record.getLength()-record.getHeaderSize())-4];
					System.arraycopy(fullPayLoad, 4, payLoad, 0, payLoad.length);				
					FileOutputStream outputStream = new FileOutputStream(targetFile);
					outputStream.write(ByteBuffer.wrap(payLoad).order(ByteOrder.LITTLE_ENDIAN).array());
					outputStream.close();
				} else if(new String(ByteBuffer.wrap(shortHeader).order(ByteOrder.LITTLE_ENDIAN).array(),"UTF-8").equals("*")) {
					byte[] payLoad = new byte[(int)(record.getLength()-record.getHeaderSize())-1];	
					System.arraycopy(fullPayLoad, 1, payLoad, 0, payLoad.length);	
					System.out.println("String: "+ new String(ByteBuffer.wrap(payLoad).order(ByteOrder.LITTLE_ENDIAN).array(),"UTF-8"));
				} else {			
					byte[] payLoad = new byte[(int)(record.getLength()-record.getHeaderSize())-4];				
					System.arraycopy(fullPayLoad, 4, payLoad, 0, payLoad.length);				
					int expectedBrotliSizeLittle = ByteBuffer.wrap(brotliHeader).order(ByteOrder.LITTLE_ENDIAN).getInt();
					byte[] uncompressedLittleEndian = new byte[expectedBrotliSizeLittle];			
					BrotliInputStream brotliLittleEndian = new BrotliInputStream(new ByteArrayInputStream(ByteBuffer.wrap(payLoad).order(ByteOrder.LITTLE_ENDIAN).array()));
					brotliLittleEndian.read(uncompressedLittleEndian);			
					brotliLittleEndian.close();
					FileOutputStream outputStream = new FileOutputStream(targetFile);
					outputStream.write(uncompressedLittleEndian);	
					outputStream.close();
				}
			}
			dataIn.close();
		}
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
