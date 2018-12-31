package main.code.parse;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

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
			byte[] header  = new byte[4];
			dataIn.readFully(header);
			byte[] payload = new byte[(int)(retrievedA.getLength()-retrievedA.getHeaderSize()-4)];
			dataIn.readFully(payload);	
			int expectedBrotliSizeLittle = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN).getInt();
			uncompressedLittleEndian = new byte[expectedBrotliSizeLittle];	
			byte[] payLoadLittleEndian  = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN).array();
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
