package main.code.record;

import java.nio.ByteOrder;

public class Payload {
	
	private PayloadType payloadType = PayloadType.NOT_DEFINED;	
	private ByteOrder byteOrder = null;
	private CompressionType compressionType = CompressionType.NOT_DEFINED;
	private long startBit = 0L;
	private int length = 0;
	private int expectedDecompressedLength = 0;
	private String targetPath = "";
		
	public Payload() {
		
	}
	
	public void printToConsole() {
		System.out.println("Payload type: "+payloadType.getStringRepresentation());
		System.out.println("Payload compression type: "+compressionType.getStringRepresentation());
		System.out.println("Payload start bit: "+startBit);
		System.out.println("Payload uncompressed length: "+length);
		System.out.println("Payload expected decompressed length if applicable: "+expectedDecompressedLength);
	}

	public PayloadType getPayloadType() {
		return payloadType;
	}

	public void setPayloadType(PayloadType payloadType) {
		this.payloadType = payloadType;
	}

	public ByteOrder getByteOrder() {
		return byteOrder;
	}

	public void setByteOrder(ByteOrder byteOrder) {
		this.byteOrder = byteOrder;
	}

	public CompressionType getCompressionType() {
		return compressionType;
	}

	public void setCompressionType(CompressionType compressionType) {
		this.compressionType = compressionType;
	}

	public long getStartBit() {
		return startBit;
	}

	public void setStartBit(long startBit) {
		this.startBit = startBit;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getExpectedDecompressedLength() {
		return expectedDecompressedLength;
	}

	public void setExpectedDecompressedLength(int expectedDecompressedLength) {
		this.expectedDecompressedLength = expectedDecompressedLength;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}
	
}
