package main.code.parse;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import main.code.record.CompressionType;
import main.code.record.Payload;
import main.code.record.PayloadType;
import main.code.record.Record;

public class GGPKPayloadParser {
	
	/**
	 * Method used to capture all relevant meta data for the pay-load of a specific GGPK record.
	 * In case of DDS files there are three options:
	 * 1) The payload is a not-compressed DDS files.
	 * 2) The payload is a reference to another file.
	 * 3) The payload is a compressed DDS file.
	 * @param record the record with which the pay-load is associated.
	 * @param dataIn the binary data stream which is used to extract the data from.
	 * @throws IOException thrown if target file cannot be written to, or the source file cannot be read from.
	 */
	public void processDDS(Record record,DataInputStream dataIn) throws IOException {
		Payload payload = new Payload();
		payload.setPayloadType(PayloadType.DDS);
		payload.setByteOrder(ByteOrder.LITTLE_ENDIAN);
		record.setPayload(payload);
		byte[] bytesRead = new byte[(int) (record.getLength()-record.getHeaderSize())];		
		dataIn.readFully(bytesRead);
		byte[] brotliHeader = new byte[4];
		System.arraycopy(bytesRead, 0, brotliHeader, 0, 4);	
		byte[] shortHeader = new byte[1];
		System.arraycopy(brotliHeader, 0, shortHeader, 0, 1);		
		if(new String(ByteBuffer.wrap(brotliHeader).order(ByteOrder.LITTLE_ENDIAN).array(),"UTF-8").equals("DDS ")) {			
			payload.setStartBit(record.getStartBit()+record.getHeaderSize());
			payload.setCompressionType(CompressionType.NOT_COMPRESSED);		
		} else if(new String(ByteBuffer.wrap(shortHeader).order(ByteOrder.LITTLE_ENDIAN).array(),"UTF-8").equals("*")) {
			payload.setStartBit(record.getStartBit()+record.getHeaderSize()+1);
			payload.setCompressionType(CompressionType.REFERENCE);			
		} else {
			payload.setStartBit(record.getStartBit()+record.getHeaderSize()+4);
			payload.setCompressionType(CompressionType.BROTLI);
			payload.setExpectedDecompressedLength(ByteBuffer.wrap(brotliHeader).order(ByteOrder.LITTLE_ENDIAN).getInt());			
		}
		payload.setLength((int) (record.getStartBit()+record.getLength()-payload.getStartBit()));
	}
}
