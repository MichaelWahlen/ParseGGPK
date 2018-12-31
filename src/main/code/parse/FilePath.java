package main.code.parse;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FilePath {
	
	public void setAbsolutePath(String pathToDirectory, Map<Long, Record> records) throws ValidationException, IOException {
		File file = new File(pathToDirectory);
		Record rootRecord = records.get(0L);		
		for(long reference:rootRecord.getReferences()) {
			Record foundReference = records.get(reference);
			if(foundReference==null) {
				throw new ValidationException("Reference not found");	
			}
			if(foundReference.getTag()==RecordTypes.DIRECTORY){										
					foundReference.setAbsoluteTargetFilePath(new File(file,"ROOT").getAbsolutePath());						
					recursionDirectory(foundReference, records);					
			}
		}
		setPathForFilesNotReferencedFromRoot(pathToDirectory, records);
	}

	private void recursionDirectory(Record parentRecord, Map<Long, Record> records) throws ValidationException, IOException {
		List<Long> containedReferences = parentRecord.getReferences();		
		if(containedReferences.size()!=0) {		
			for(Long reference: containedReferences) {							
				Record foundRecord = records.get(reference);
				if(foundRecord==null) {
					throw new ValidationException("Reference not found");	
				} else {
					String name = foundRecord.getName();	
					File directory = new File(parentRecord.getAbsoluteTargetFilePath(),name);						
					foundRecord.setAbsoluteTargetFilePath(directory.getAbsolutePath());			
					if(foundRecord.getTag()==RecordTypes.DIRECTORY) {							
						recursionDirectory(foundRecord,records);
					}
					if(foundRecord.getTag()==RecordTypes.FILE) {	
						foundRecord.setHasReference(true);
					}
				}
			}
		}
	}

	private void setPathForFilesNotReferencedFromRoot(String pathToDirectory, Map<Long, Record> records) throws ValidationException, IOException {	
		File rootFolder = new File(pathToDirectory,"NOTROOT");		
		for(Record record:records.values()) {
			if(!record.hasPathSet()&&!record.isHasReference()&&record.getTag()==RecordTypes.DIRECTORY) {				
				File filePath = new File(rootFolder,record.getName());
				record.setAbsoluteTargetFilePath(filePath.getAbsolutePath());
				record.hasPathSet();
				recursionDirectory(record, records);
			}
		}
		setUnreferencedFiles(pathToDirectory, records);
	}

	private void setUnreferencedFiles(String pathToDirectory, Map<Long, Record> records) {
		File unreferencedFolder = new File(pathToDirectory,"UNREFERENCED");		
		long count = 0;
		for(Record record:records.values()) {
			if(!record.hasPathSet()&&record.getTag()==RecordTypes.FILE) {
				File filePath = new File(unreferencedFolder,record.getName());
				record.setAbsoluteTargetFilePath(filePath.getAbsolutePath());
				record.hasPathSet();	
				count++;
			}
		}
		System.out.println("Orphaned files: " + count);
	}
	
	
}
