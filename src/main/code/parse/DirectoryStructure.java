package main.code.parse;

import java.io.File;
import java.util.Map;

public class DirectoryStructure {
	
	private Map<Long, Record> records;
	
	public DirectoryStructure(Map<Long, Record> records) {
		this.records =records;
	}
	
	public void buildDirectoryStructure(String pathToDirectory) throws ValidationException {
		File file = new File(pathToDirectory);
		if(file.exists()) {
			throw new ValidationException("Target directory already exists");			
		} else {
			Record rootRecord = records.get(0L);
			for(long reference:rootRecord.getReferences()) {
				Record foundReference = records.get(reference);
				if(foundReference==null) {
					throw new ValidationException("Reference not found");	
				}
				if(foundReference.getTag().equals("PDIR")){
					file.mkdirs();	
					String pdirName = foundReference.getName();
					if(pdirName.length()<1) {
						pdirName = "ROOT";
					}
					new File(file,pdirName).mkdir();
				}
			}
		}
	}
	
}
