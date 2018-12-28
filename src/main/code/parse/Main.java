package main.code.parse;

import java.io.DataInputStream;
import java.io.File;
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
		FilePath structure = new FilePath();
		structure.setAbsolutePath("C:\\ggpkextract", records);
		Map<String, Record> recordsByName  = new HashMap<String, Record>();
		for(Record record:records.values()) {
			if(record.getTag().equals("FILE")||record.getTag().equals("PDIR")) {
				recordsByName.put(record.getAbsoluteTargetFilePath(), record);				
			}
		}		
		TieTree<Record> tree = new TieTree<Record>();
		tree.addEntries(recordsByName);
		DataInputStream dataIn = new DataInputStream(new FileInputStream("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk"));
		BinaryWriter binaryWriter = new BinaryWriter();
		Record retrievedA = tree.getObject("C:\\ggpkextract\\ROOT\\Metadata\\UI\\InGameState\\Tutorials\\longtext.ui");
		binaryWriter.write(retrievedA.getStartMarker(), retrievedA.getLength(), retrievedA.getHeaderSize(), dataIn, new File(retrievedA.getAbsoluteTargetFilePath()));
		dataIn = new DataInputStream(new FileInputStream("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk"));
		retrievedA = tree.getObject("C:\\ggpkextract\\ROOT\\Metadata\\UI\\InGameState\\HUD\\HUD.Tencent.ui");
		binaryWriter.write(retrievedA.getStartMarker(), retrievedA.getLength(), retrievedA.getHeaderSize(), dataIn, new File(retrievedA.getAbsoluteTargetFilePath()));
	}

}
