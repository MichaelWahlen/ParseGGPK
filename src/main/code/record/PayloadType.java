package main.code.record;

public enum PayloadType {
	DDS("dds"), NOT_DEFINED("not defined");	
	
	private String stringRepresentation;
	
	private PayloadType(String string) {
		stringRepresentation = string;
	}
	
	public String getStringRepresentation() {
		return stringRepresentation;
	}
	
}
