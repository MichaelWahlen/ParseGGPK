package main.code.parse;

import java.io.File;
import java.util.List;
import java.util.Map;

public class FilePath {
	
	private final String topLevelRootDirectoryName = "root";
	private final String topLevelNotRootDirectoryName = "not_root";
	
	/**
	 * Class serves to provide the GGPK records with their respective absolute path. <br>
	 * There are 3 separate cases that need to be considered: <br>
	 * 1) All files that can be drilled down to from the GGPK root directory (GGPK record) <br>
	 * 2) All files that cannot be found when drilling down from the GGPK root directory, but that do have a parent directory (PDIR record). <br>
	 * 3) All remaining files that do not seem to have a parent directory. <br>
	 * It's important to note that the first record in the provided record set/map is expected to be the root record (GGPK). <br>
	 * Further note: As it known which treatment a record should get (out of the above 3), there are three complete cycles made through the entire set of records. <br>
	 * This can be improved by removing the finalized records, but that would require a copy of the set, as the original set should remain intact.
	 * @param pathToTargetDirectory the target directory absolute file path, as String
	 * @param records all records that need to be provided with an absolute path.
	 * @throws ValidationException thrown when an assumption made about the provided record proofs false
	 */
	public void setAbsolutePath(String pathToTargetDirectory, Map<Long, Record> records) throws ValidationException {
		File file = new File(pathToTargetDirectory);
		Record rootRecord = records.get(0L);
		if(rootRecord.getRecordType()!=RecordType.ROOT) {
			throw new ValidationException("First record in the map is not the ROOT record.");	
		} else {
			for(long reference:rootRecord.getReferences()) {
				Record foundReference = records.get(reference);
				if(foundReference==null) {
					throw new ValidationException("Reference in root record cannot be found");	
				}
				if(foundReference.getRecordType()==RecordType.DIRECTORY){										
						foundReference.setAbsoluteTargetFilePath(new File(file,topLevelRootDirectoryName).getAbsolutePath());						
						recursionDirectory(foundReference, records);					
				}
			}
		}	
		// for all files and directories that are cannot be drilled down to from the root we use separate handling.
		setRemainingPaths(pathToTargetDirectory, records);
	}
	
	/**
	 * Method used to recursively cycle through a directory structure. Every found file is provided with its absolute path, while every found directory is being fed back into the method. 
	 * This means that child-less directories are not provided with a path here.
	 * @param parentRecord
	 * @param records
	 * @throws ValidationException
	 */	
	private void recursionDirectory(Record parentRecord, Map<Long, Record> records) throws ValidationException {
		List<Long> containedReferences = parentRecord.getReferences();		
		if(containedReferences.size()!=0) {		
			for(Long reference: containedReferences) {							
				Record foundRecord = records.get(reference);
				if(foundRecord==null) {
					throw new ValidationException("PDIR record with an reference file which cannot be retrieved.\n" + "Parent record name: "+parentRecord.getName());	
				} else {
					String name = foundRecord.getName();	
					File directory = new File(parentRecord.getAbsoluteTargetFilePath(),name);						
					foundRecord.setAbsoluteTargetFilePath(directory.getAbsolutePath());			
					if(foundRecord.getRecordType()==RecordType.DIRECTORY) {							
						recursionDirectory(foundRecord,records);
					}
					if(foundRecord.getRecordType()==RecordType.FILE) {	
						foundRecord.setHasReference(true);
					}
				}
			}
		}
	}
	
	
	/**
	 * Entry point for the the handling of all files and directories not touched when drilling down from the top level root GGPK record.
	 * @param pathToDirectory the target directory absolute file path, as String
	 * @param records the parsed records
	 */
	private void setRemainingPaths(String pathToDirectory, Map<Long, Record> records) {	
		File notRootTopLevelFolder = new File(pathToDirectory,topLevelNotRootDirectoryName);		
		for(Record record:records.values()) {
			if(!record.hasPathSet()&&!record.isHasReference()&&record.getRecordType()==RecordType.DIRECTORY) {				
				File filePath = new File(notRootTopLevelFolder,record.getName());
				record.setAbsoluteTargetFilePath(filePath.getAbsolutePath());
				record.hasPathSet();
				try {
					recursionDirectory(record, records);
				} catch (ValidationException e) {					
					e.printStackTrace();
				}
			}
		}
		// for all files that could not be provided with a parent directory, we use the following method
		setUnreferencedFiles(pathToDirectory, records);
	}
	
	
	/**
	 * All files where no parent directory could be found are placed directly under the non-GGPK root folder.
	 * @param pathToDirectory the target directory absolute file path, as String
	 * @param records the parsed records
	 */
	private void setUnreferencedFiles(String pathToDirectory, Map<Long, Record> records) {
		File notRootTopLevelFolder = new File(pathToDirectory,topLevelNotRootDirectoryName);		
		long count = 0;
		for(Record record:records.values()) {
			if(!record.hasPathSet()&&record.getRecordType()==RecordType.FILE) {
				File filePath = new File(notRootTopLevelFolder,record.getName());
				record.setAbsoluteTargetFilePath(filePath.getAbsolutePath());
				record.hasPathSet();	
				count++;
			}
		}
		System.out.println("Orphaned files: " + count);
	}
	
	
}
