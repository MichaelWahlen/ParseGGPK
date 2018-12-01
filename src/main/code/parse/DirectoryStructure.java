package main.code.parse;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DirectoryStructure {
	
	private Map<Long, Record> records;
	
	public DirectoryStructure(Map<Long, Record> records) {
		this.records =records;
	}
	
	public void buildDirectoryStructure(String pathToDirectory) throws ValidationException, IOException {
		File file = new File(pathToDirectory);
		if(file.exists()) {
			throw new ValidationException("Target directory already exists");			
		} else {
			Record rootRecord = records.get(0L);
			file.mkdirs();	
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
						
						recursionDirectory(foundReference);
					
					
				}
			}
		}
	}

	private void recursionDirectory(Record parentRecord) throws ValidationException, IOException {
		List<Long> containedReferences = parentRecord.getReferences();
		Map<Long, Record> records = getRecords();
		if(containedReferences.size()==0) {
			
		} else {
			for(Long reference: containedReferences) {
				Record foundRecord = records.get(reference);
				if(foundRecord==null) {
					throw new ValidationException("Reference not found");	
				} else {
					if(foundRecord.getTag().equals("PDIR")) {
						String pdirName = foundRecord.getName();
						pdirName = pdirName.replaceAll("[^A-Za-z0-9]", "");
						File directory = new File(parentRecord.getAbsoluteTargetFilePath(),pdirName);						
						foundRecord.setAbsoluteTargetFilePath(directory.getAbsolutePath());
						directory.mkdirs();
						recursionDirectory(foundRecord);
					} else if(foundRecord.getTag().equals("FILE")) {
						String fileName = foundRecord.getName();
						fileName = fileName.replaceAll("[^A-Za-z0-9.]", "");
						File directory = new File(parentRecord.getAbsoluteTargetFilePath(),fileName);						
						foundRecord.setAbsoluteTargetFilePath(directory.getAbsolutePath());
						directory.createNewFile();
					}
				}
			}
		}
	}

	private Map<Long, Record> getRecords() {		
		return records;
	}
	
	
	
}
