package main.code.parse;

import java.util.List;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import main.code.datastructure.TieTree;
import main.code.gui.ViewOnGGPK;

public class Main {
	


	public static void main(String[] args) throws ValidationException, IOException {	
		long startTime = System.nanoTime();		
		ParseGGPK parse = new ParseGGPK();		
		parse.parseGGPK("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk");
		Map<Long, Record> records  = parse.getRecords();
		FilePath structure = new FilePath();
		structure.setAbsolutePath("C:\\ggpkextract", records);
		Map<String, Record> recordsByName  = new HashMap<String, Record>();
		for(Record record:records.values()) {
			if(record.getRecordType()==RecordType.FILE||record.getRecordType()==RecordType.DIRECTORY) {
				recordsByName.put(record.getAbsoluteTargetFilePath(), record);				
			}
		}		
		TieTree<Record> tree = new TieTree<Record>();
		tree.addEntries(recordsByName);
		DataInputStream dataIn = new DataInputStream(new FileInputStream("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk"));
		BinaryWriter binaryWriter = new BinaryWriter();
		Record retrievedA = tree.getObject("C:\\ggpkextract\\ROOT\\Metadata\\UI\\InGameState\\Tutorials\\longtext.ui");
		binaryWriter.write(retrievedA, dataIn);
		dataIn = new DataInputStream(new FileInputStream("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk"));
		retrievedA = tree.getObject("C:\\ggpkextract\\ROOT\\Metadata\\UI\\InGameState\\HUD\\HUD.Tencent.ui");
		binaryWriter.write(retrievedA, dataIn);
		dataIn = new DataInputStream(new FileInputStream("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk"));
		retrievedA = tree.getObject("C:\\ggpkextract\\NOTROOT\\double_strike\\slash_base.dds");
		binaryWriter.write(retrievedA, dataIn);
		dataIn = new DataInputStream(new FileInputStream("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk"));
		retrievedA = tree.getObject("C:\\ggpkextract\\ROOT\\Art\\scrolling_blue_hotmetal.dds");
		binaryWriter.write(retrievedA, dataIn);
		dataIn = new DataInputStream(new FileInputStream("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk"));
		retrievedA = tree.getObject("C:\\ggpkextract\\ROOT\\Art\\particles\\Auras\\delve_league\\CrystalWall_colour.dds");
		binaryWriter.write(retrievedA, dataIn);
		dataIn = new DataInputStream(new FileInputStream("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk"));
		retrievedA = tree.getObject("C:\\ggpkextract\\NOTROOT\\FX\\Glow.dds");
		binaryWriter.write(retrievedA, dataIn);
		dataIn = new DataInputStream(new FileInputStream("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk"));
		retrievedA = tree.getObject("C:\\ggpkextract\\ROOT\\Art\\scrolling_hotmetal.dds");
		binaryWriter.write(retrievedA, dataIn);

//		for(String key:tree.getPrefixedKeys("C:\\ggpkextract\\ROOT\\Art")) {
//			dataIn = new DataInputStream(new FileInputStream("C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk"));
//			retrievedA = tree.getObject(key);
//			binaryWriter.write(retrievedA, dataIn);
//		}
//	
	
		// code to enable a tree overview that makes sense to the human eye
		List<String> allKeys = tree.getPrefixedKeys("");
		List<List<String>> deconstructedKeys = new ArrayList<List<String>>();
		for(String key:allKeys) {
			List<String> deconstructedKey = new ArrayList<String>();			
			StringBuilder stringPart = new StringBuilder();
			for(char charz:key.toCharArray()) {				
				if(charz=='\\'){
					deconstructedKey.add(stringPart.toString());
					stringPart.setLength(0);
				} else {
					stringPart.append(charz);
				}
			}
			if(stringPart.length()>0) {
				deconstructedKey.add(stringPart.toString());
			}
			deconstructedKeys.add(deconstructedKey);
		}
		InnerNode iNode = new InnerNode(deconstructedKeys);
		 SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                new ViewOnGGPK(iNode.getTopLevelJTreeNode());
	            }
	        });
		
		System.out.println("Finished in "+((System.nanoTime() - startTime)/1000000)/1000+" seconds.");
	}

}
