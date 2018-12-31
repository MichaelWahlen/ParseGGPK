package main.code.parse;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.brotli.dec.BrotliInputStream;



public class BinaryWriter {
	
	public void write(Record retrievedA, DataInputStream dataIn) throws IOException,FileNotFoundException {
		dataIn.skip(retrievedA.getStartMarker()+retrievedA.getHeaderSize());
		File targetFile = new File(retrievedA.getAbsoluteTargetFilePath());
		targetFile.getParentFile().mkdirs();
		targetFile.createNewFile();
		retrievedA.printToConsole();
		byte[] uncompressedLittleEndian = null;
		if(targetFile.getName().contains(".dds")) {			
			byte[] fullPayLoad = new byte[(int)(retrievedA.getLength()-retrievedA.getHeaderSize())];		
			dataIn.readFully(fullPayLoad);			
			byte[] brotliHeader = new byte[4];
			System.arraycopy(fullPayLoad, 0, brotliHeader, 0, 4);			
			byte[] shortHeader = new byte[1];
			System.arraycopy(shortHeader, 0, brotliHeader, 0, 1);			
			if(new String(brotliHeader,"UTF-8").equals("DDS ")) {
				
			} else if(new String(shortHeader,"UTF-8").equals("*")) {
				
			} else {			
				byte[] payLoad = new byte[(int)(retrievedA.getLength()-retrievedA.getHeaderSize())-4];				
				System.arraycopy(fullPayLoad, 4, payLoad, 0, payLoad.length);				
				int expectedBrotliSizeLittle = ByteBuffer.wrap(brotliHeader).order(ByteOrder.LITTLE_ENDIAN).getInt();
				uncompressedLittleEndian = new byte[expectedBrotliSizeLittle];	
				byte[] payLoadLittleEndian  = ByteBuffer.wrap(payLoad).order(ByteOrder.LITTLE_ENDIAN).array();
				InputStream wrappedPayLoadStreamLittleEndian = new ByteArrayInputStream(payLoadLittleEndian);	
				BrotliInputStream brotliLittleEndian = new BrotliInputStream(wrappedPayLoadStreamLittleEndian);
				brotliLittleEndian.read(uncompressedLittleEndian);			
				brotliLittleEndian.close();
				FileOutputStream outputStream = new FileOutputStream(targetFile);
				outputStream.write(uncompressedLittleEndian);	
				outputStream.close();
			}
		}
		
	}

	
	
}
