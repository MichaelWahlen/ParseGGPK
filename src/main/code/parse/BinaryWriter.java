package main.code.parse;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.brotli.dec.BrotliInputStream;



public class BinaryWriter {
	
	public void write(Record retrievedA, DataInputStream dataIn) throws IOException,FileNotFoundException {
		if(retrievedA.getRecordType()==RecordType.FILE) {
			dataIn.skip(retrievedA.getStartMarker()+retrievedA.getHeaderSize());
			File targetFile = new File(retrievedA.getAbsoluteTargetFilePath());
			targetFile.getParentFile().mkdirs();		
			targetFile.createNewFile();		
			//retrievedA.printToConsole();		
			if(targetFile.getName().contains(".dds")) {			
				byte[] fullPayLoad = new byte[(int)(retrievedA.getLength()-retrievedA.getHeaderSize())];		
				dataIn.readFully(fullPayLoad);			
				byte[] brotliHeader = new byte[4];
				System.arraycopy(fullPayLoad, 0, brotliHeader, 0, 4);			
				byte[] shortHeader = new byte[1];
				System.arraycopy(fullPayLoad, 0, shortHeader, 0, 1);			
				
				if(new String(ByteBuffer.wrap(brotliHeader).order(ByteOrder.LITTLE_ENDIAN).array(),"UTF-8").equals("DDS ")) {
					byte[] payLoad = new byte[(int)(retrievedA.getLength()-retrievedA.getHeaderSize())-4];
					System.arraycopy(fullPayLoad, 4, payLoad, 0, payLoad.length);				
					FileOutputStream outputStream = new FileOutputStream(targetFile);
					outputStream.write(ByteBuffer.wrap(payLoad).order(ByteOrder.LITTLE_ENDIAN).array());
					outputStream.close();
				} else if(new String(ByteBuffer.wrap(shortHeader).order(ByteOrder.LITTLE_ENDIAN).array(),"UTF-8").equals("*")) {
					byte[] payLoad = new byte[(int)(retrievedA.getLength()-retrievedA.getHeaderSize())-1];	
					System.arraycopy(fullPayLoad, 1, payLoad, 0, payLoad.length);	
					System.out.println("String: "+ new String(ByteBuffer.wrap(payLoad).order(ByteOrder.LITTLE_ENDIAN).array(),"UTF-8"));
				} else {			
					byte[] payLoad = new byte[(int)(retrievedA.getLength()-retrievedA.getHeaderSize())-4];				
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
		}
	}

	
	
}
