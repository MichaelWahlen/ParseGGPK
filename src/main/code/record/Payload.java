package main.code.record;

import java.nio.ByteOrder;

public class Payload {
	
	private PayloadType payloadType = PayloadType.NOT_DEFINED;	
	private ByteOrder byteOrder = null;
	private CompressionType compressionType = CompressionType.NOT_DEFINED;
	private long startBit = 0L;
	private long length = 0L;
		
	public Payload() {
		
	}
	
	public void printToConsole() {
		System.out.println("payloadType: "+payloadType.getStringRepresentation());
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

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}
	
}
