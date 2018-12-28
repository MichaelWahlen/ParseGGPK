package main.code.parse;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BinaryWriter {
	
	public void write(long startMarker, long length, long headerSize,DataInputStream dataIn, File targetFile) throws IOException,FileNotFoundException {
		dataIn.skip(startMarker);
		targetFile.getParentFile().mkdirs();
		targetFile.createNewFile();
		byte[] bytesRead = new byte[(int) (length-headerSize)];		
		dataIn.readFully(bytesRead);
		FileOutputStream outputStream = new FileOutputStream(targetFile);
		outputStream.write(bytesRead);	
		outputStream.close();
	}
	
	
}
