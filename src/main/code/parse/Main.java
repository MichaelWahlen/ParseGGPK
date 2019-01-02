package main.code.parse;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;

import main.code.datastructure.TieTree;
import main.code.gui.ViewOnGGPK;
import main.code.record.Record;
import main.code.record.RecordType;

public class Main {
	
	private final static String fileLocation = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Path of Exile\\Content.ggpk";
	
	public static void main(String[] args) throws ValidationException, IOException {	
		long startTime = System.nanoTime();		
		GGPKHeaderParser parse = new GGPKHeaderParser();		
		parse.parseGGPK(fileLocation);
		Map<Long, Record> records  = parse.getRecords();
		FilePath structure = new FilePath("C:\\ggpkextract\\root","C:\\ggpkextract\\not_root");
		structure.setAbsolutePath(records);
		Map<String, Record> recordsByName  = new HashMap<String, Record>();
		for(Record record:records.values()) {
			if(record.getRecordType()==RecordType.FILE||record.getRecordType()==RecordType.DIRECTORY) {
				recordsByName.put(record.getAbsoluteTargetFilePath(), record);				
			}
		}		
		TieTree<Record> tree = new TieTree<Record>();
		tree.addEntries(recordsByName);
		List<Record> records2 = new ArrayList<Record>();
		BinaryPayloadProcessor binaryWriter = new BinaryPayloadProcessor(fileLocation);
		Record retrievedA = tree.getObject("C:\\ggpkextract\\root\\Metadata\\UI\\InGameState\\Tutorials\\longtext.ui");
		records2.add(retrievedA);
		retrievedA = tree.getObject("C:\\ggpkextract\\root\\Metadata\\UI\\InGameState\\HUD\\HUD.Tencent.ui");
		records2.add(retrievedA);
		retrievedA = tree.getObject("C:\\ggpkextract\\not_root\\double_strike\\slash_base.dds");
		records2.add(retrievedA);
		retrievedA = tree.getObject("C:\\ggpkextract\\root\\Art\\scrolling_blue_hotmetal.dds");
		records2.add(retrievedA);
		retrievedA = tree.getObject("C:\\ggpkextract\\root\\Art\\particles\\Auras\\delve_league\\CrystalWall_colour.dds");
		records2.add(retrievedA);
		retrievedA = tree.getObject("C:\\ggpkextract\\not_root\\FX\\Glow.dds");
		records2.add(retrievedA);
		retrievedA = tree.getObject("C:\\ggpkextract\\root\\Art\\scrolling_hotmetal.dds");
		records2.add(retrievedA);	
		retrievedA = tree.getObject("C:\\ggpkextract\\root\\Art\\2DArt\\Cubemaps\\Overcast01_diffuse.dds");
		records2.add(retrievedA);		
		binaryWriter.write(records2);
		
		
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
