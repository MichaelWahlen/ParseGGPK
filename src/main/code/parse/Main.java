package main.code.parse;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import main.code.datastructure.TieTree;

public class Main {

	public static void main(String[] args) throws ValidationException, IOException {	
		ParseGGPK parse = new ParseGGPK();		
		parse.parseGGPK("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk");
		Map<Long, Record> records  = parse.getRecords();
		SetAbsoluteFilePath structure = new SetAbsoluteFilePath();
		structure.setAbsolutePath("C:\\ggpkextract", records);
		Map<String, Record> recordsByName  = new HashMap<String, Record>();
		for(Record record:records.values()) {
			if(record.getTag().equals("FILE")||record.getTag().equals("PDIR")) {
				recordsByName.put(record.getAbsoluteTargetFilePath(), record);				
			}
		}		
		TieTree tree = new TieTree();
		tree.addStrings(recordsByName);
		for(String string:tree.getAllStrings(new String("C:\\ggpkextract\\ROOT\\Metadata").toCharArray())) {
			System.out.println(string);
		}
		DataInputStream dataIn = new DataInputStream(new FileInputStream("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk"));
		tree.writeRecord(new String("C:\\ggpkextract\\ROOT\\Metadata\\UI\\InGameState\\Tutorials\\longtext.ui").toCharArray(), dataIn);
		tree.writeRecord(new String("C:\\ggpkextract\\ROOT\\Metadata\\UI\\InGameState\\HUD\\HUD.Tencent.ui").toCharArray(), dataIn);
	}

}
