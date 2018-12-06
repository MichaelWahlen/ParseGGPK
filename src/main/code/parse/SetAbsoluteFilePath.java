package main.code.parse;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SetAbsoluteFilePath {
	
	public void setAbsolutePath(String pathToDirectory, Map<Long, Record> records) throws ValidationException, IOException {
		File file = new File(pathToDirectory);
		Record rootRecord = records.get(0L);		
		for(long reference:rootRecord.getReferences()) {
			Record foundReference = records.get(reference);
			if(foundReference==null) {
				throw new ValidationException("Reference not found");	
			}
			if(foundReference.getTag().equals("PDIR")){					
					String pdirName = "ROOT";
					File rootDirectory = new File(file,pdirName);
					rootDirectory.mkdir();						
					foundReference.setAbsoluteTargetFilePath(rootDirectory.getAbsolutePath());						
					recursionDirectory(foundReference, records);					
			}
		}
		createPathForNotReferencedFiles(pathToDirectory, records);
	}

	private void createPathForNotReferencedFiles(String pathToDirectory, Map<Long, Record> records) {
		File dumpFolder = new File(pathToDirectory,"DUMP");
		for(Record record:records.values()) {
			if(!record.hasPathSet()&&record.getTag().equals("FILE")) {
				File filePath = new File(dumpFolder,record.getName().replaceAll("[^A-Za-z0-9.]", ""));
				record.setAbsoluteTargetFilePath(filePath.getAbsolutePath());
			}
		}
		
	}

	private void recursionDirectory(Record parentRecord, Map<Long, Record> records) throws ValidationException, IOException {
		List<Long> containedReferences = parentRecord.getReferences();		
		if(containedReferences.size()!=0) {		
			for(Long reference: containedReferences) {							
				Record foundRecord = records.get(reference);
				if(foundRecord==null) {
					throw new ValidationException("Reference not found");	
				} else {
					String name = foundRecord.getName().replaceAll("[^A-Za-z0-9.]", "");			
					File directory = new File(parentRecord.getAbsoluteTargetFilePath(),name);						
					foundRecord.setAbsoluteTargetFilePath(directory.getAbsolutePath());			
					if(foundRecord.getTag().equals("PDIR")) {							
						recursionDirectory(foundRecord,records);
					} 
				}
			}
		}
	}

	
	
	
}
