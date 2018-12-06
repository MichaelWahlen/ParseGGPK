package main.code.parse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {

	public static void main(String[] args) throws ValidationException, IOException {	
		ParseGGPK parse = new ParseGGPK("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk");		
		parse.parseGGPK();
		Map<Long, Record> records  = parse.getRecords();
		SetAbsoluteFilePath structure = new SetAbsoluteFilePath();
		structure.setAbsolutePath("C:\\ggpkextract", records);
		Map<String, Record> recordsByName  = new HashMap<String, Record>();
		for(Record record:records.values()) {
			if(record.getTag().equals("FILE")||record.getTag().equals("PDIR")) {
				recordsByName.put(record.getAbsoluteTargetFilePath(), record);
				System.out.println(record.getAbsoluteTargetFilePath());
			}
		}
		
		
		//DataInputStream dataIn = new DataInputStream(new FileInputStream("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk"));
		//Writer writer = new Writer(dataIn,parse.getRecords());
	}

}
