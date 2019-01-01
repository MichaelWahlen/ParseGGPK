package main.code.parse;

import java.util.Map;

import main.code.record.Record;
import main.code.record.RecordType;

public class FilePath {
	
	private final String topLevelRootDirectoryName;
	private final String topLevelNotRootDirectoryName;
	
	/**
	 * Class used to provided records with their absolute location file path. Assumes a GGPK record structure of folders and files.
	 * @param targetRootFolder top level directory path for all files that can be drilled down to from the GGPK root record
	 * @param targetNonRootFolder top level directory path for all files that can not be drilled down to from GGPK root record
	 */
	public FilePath(String targetRootFolder, String targetNonRootFolder) {
		topLevelRootDirectoryName = targetRootFolder;
		topLevelNotRootDirectoryName = targetNonRootFolder;
	}	
	
	/**
	 * Method serves to provide the GGPK records with their respective absolute path. <br>
	 * There are 3 separate cases that need to be considered: <br>
	 * 1) All files that can be drilled down to from the GGPK root directory (GGPK record) <br>
	 * 2) All files that cannot be found when drilling down from the GGPK root directory, but that do have a parent directory (PDIR record). <br>
	 * 3) All remaining files that do not seem to have a parent directory. <br>
	 * It's important to note that the first record in the provided record set/map is expected to be the root record (GGPK). <br>
	 * Further note: As it known which treatment a record should get (out of the above 3), there are three complete cycles made through the entire set of records. <br>
	 * This can be improved by removing the finalized records, but that would require a copy of the set, as the original set should remain intact. It would also create some concurrency issues that would need to be handled.
	 * @param pathToTargetDirectory the target directory absolute file path, as String
	 * @param records all records that need to be provided with an absolute path.
	 * @throws ValidationException thrown when an assumption made about the provided record proofs false
	 */
	public void setAbsolutePath(Map<Long, Record> records) throws ValidationException {
		Record rootRecord = records.get(0L);
		if(rootRecord.getRecordType()!=RecordType.ROOT) {
			throw new ValidationException("First record in the map is not the ROOT record.");	
		} else {
			for(long referenceKey:rootRecord.getReferences()) {
				Record foundReference = records.get(referenceKey);
				if(foundReference==null) {
					throw new ValidationException("Reference in root record cannot be found");	
				}
				if(foundReference.getRecordType()==RecordType.DIRECTORY){										
						foundReference.setAbsoluteTargetFilePath(topLevelRootDirectoryName);						
						recursionDirectory(foundReference, records);					
				}
			}
		}	
		// for all files and directories that are cannot be drilled down to from the root we use separate handling.
		setRemainingPaths(records);
	}
	
	/**
	 * Method used to recursively cycle through a directory structure. Every found file is provided with its absolute path, while every found directory is being fed back into the method. 
	 * This means that child-less directories are not provided with a path here.
	 * @param parentRecord
	 * @param records
	 * @throws ValidationException thrown is a file which is being referenced as contained in a directory, cannot be found
	 */	
	private void recursionDirectory(Record parentRecord, Map<Long, Record> records) throws ValidationException {	
		for(Long referenceKey: parentRecord.getReferences()) {							
			Record foundRecord = records.get(referenceKey);
			if(foundRecord==null) {
				throw new ValidationException("PDIR record with an reference file which cannot be retrieved.\n" + "Parent record name: "+parentRecord.getName());	
			} else {						
				foundRecord.setAbsoluteTargetFilePath(parentRecord.getAbsoluteTargetFilePath()+"\\"+foundRecord.getName());			
				if(foundRecord.getRecordType()==RecordType.DIRECTORY) {							
					recursionDirectory(foundRecord,records);
				}
			}
		}	
	}
	
	
	/**
	 * Entry point for the the handling of all files and directories not touched when drilling down from the top level root GGPK record.
	 * @param pathToDirectory the target directory absolute file path, as String
	 * @param records the parsed records
	 */
	private void setRemainingPaths(Map<Long, Record> records) {	
		for(Record record:records.values()) {
			if(!record.hasPathSet()&&record.getRecordType()==RecordType.DIRECTORY) {				
				record.setAbsoluteTargetFilePath(topLevelNotRootDirectoryName + "\\" + record.getName());				
				try {
					recursionDirectory(record, records);
				} catch (ValidationException e) {					
					e.printStackTrace();
				}
			}
		}
		// for all files that could not be provided with a parent directory, we use the following method
		setUnreferencedFiles(records);
	}
	
	
	/**
	 * All files where no parent directory could be found are placed directly under the non-GGPK root folder.
	 * @param pathToDirectory the target directory absolute file path, as String
	 * @param records the parsed records
	 */
	private void setUnreferencedFiles(Map<Long, Record> records) {		
		for(Record record:records.values()) {
			if(!record.hasPathSet()&&record.getRecordType()==RecordType.FILE) {				
				record.setAbsoluteTargetFilePath(topLevelNotRootDirectoryName + "\\" + record.getName());								
			}
		}
	}
	
	
}
