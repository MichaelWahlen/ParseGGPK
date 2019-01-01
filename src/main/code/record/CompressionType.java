package main.code.record;

public enum CompressionType {
	BROTLI("brotli"),NOT_DEFINED("not defined"), NOT_COMPRESSED("not compressed");
	
	private String stringRepresentation;
	
	private CompressionType(String string) {
		stringRepresentation = string;
	}
	
	public String getStringRepresentation() {
		return stringRepresentation;
	}
	
}
