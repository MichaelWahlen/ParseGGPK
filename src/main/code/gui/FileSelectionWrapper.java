package main.code.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileSelectionWrapper  {
	
	private JPanel selectionPanel = new JPanel();
	private int alignment = 0;
	private File[] files;
	private JFrame parentFrame;
	private int fileSelectionMode = 0;
	private String buttonText = "Default button";
	private String extensionFilter = null;
	private File defaultSelection = null;
	private boolean multipleSelectionsAllowed = false;
	private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	private boolean isFileSelectionSet = false;

	/** 
	 * @param parentFrame frame to tie modal window to
	 */
	public FileSelectionWrapper(JFrame parentFrame) {		
		this.parentFrame = parentFrame;		
	}
	
	/**
	 * Sets the text that should appear on the button
	 * @param buttonText
	 */
	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}
	
	public void isOnlyFileSelection() {
		this.fileSelectionMode = JFileChooser.FILES_ONLY;
	}
	
	public void isOnlyDirectorySelection() {
		this.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY;
	}
	
	public void isDirectoryAndFileSelection() {
		this.fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES;
	}
	
	/**
	 * 
	 * @param fileSelectionMode use the JFileChooser enumeration to get the correct integer value (i.e. FILES_AND_DIRECTORIES, FILES_ONLY, DIRECTORIES_ONLY)
	 * @param allowMultipleSelects
	 */
	
	public void setFileSelectionSettings(int fileSelectionMode, boolean allowMultipleSelects) {
		setFileSelectionSettings(fileSelectionMode,null,null,allowMultipleSelects);
	}	
	
	public void setFileSelectionSettings(int fileSelectionMode, String extension,File defaultSelection, boolean allowMultipleSelects) {
		this.fileSelectionMode = fileSelectionMode;
		this.extensionFilter = extension;
		this.defaultSelection = defaultSelection;
		this.multipleSelectionsAllowed = allowMultipleSelects;
		this.isFileSelectionSet = true;
	}
	
	private int getFileSelectionMode() {
		return fileSelectionMode;
	}
	
	private JFrame getParentFrame() {
		return parentFrame;
	}
	
	public void setToHorizontal() {
		alignment = 1;
	}
	
	public void setToVertical() {
		alignment = 0;
	}
	
	private void setupPane() {		
		if(getAlignment()==0) {
			selectionPanel.setLayout(new GridLayout(0,1));
		} else {
			selectionPanel.setLayout(new GridLayout(1,0));
		}
		JButton clickMe = new JButton(getButtonText());
		JTextField selectedFileName = new JTextField();
		selectedFileName.setEditable(false);
		selectionPanel.add(clickMe);
		selectionPanel.add(selectedFileName);
		if(isFileSelectionSet) {
			addListener(clickMe,selectedFileName);
		} else {
			System.out.println("Selector not set");
		}
	}
	
	private int getAlignment() {		
		return alignment;
	}

	private File[] openFile(int fileSelectionMode, File defaultSelection) {
		JFileChooser chooser = new JFileChooser(); 
	    if(getExtensionFilter()!=null) {
	    	FileNameExtensionFilter filter = new FileNameExtensionFilter(getFilterDescription(),getExtensionFilter());
	    	chooser.setFileFilter(filter);
	    	chooser.setAcceptAllFileFilterUsed(false); 
	    }
		chooser.setLocation(getHorizontalCenter(chooser.getWidth()), getVerticalCenter(chooser.getHeight())); 
		chooser.setCurrentDirectory(defaultSelection);
	    chooser.setDialogTitle(getModalTitle());
	    chooser.setMultiSelectionEnabled(isMultipleSelectionsAllowed());
	    chooser.setFileSelectionMode(fileSelectionMode);
	    if (chooser.showOpenDialog(getParentFrame()) == JFileChooser.APPROVE_OPTION) { 
	    	if(isMultipleSelectionsAllowed()) {
	    		return chooser.getSelectedFiles();
	    	} else {
	    		File[] returnFile = new File[1];
	    		returnFile[0] = chooser.getSelectedFile();
	    		return returnFile;
	    	}
	    }
	    else {
	    	return new File[1];
	    }		
	}
		
	private void addListener(JButton clickMe, JTextField selectedFileName) {		
		clickMe.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {        	
	        	files  = openFile(getFileSelectionMode(), getDefaultSelection());
	        	if(files!=null) {
	        		if(files.length==1 && files[0] !=null) {
	        			selectedFileName.setText(files[0].getName());		
	        		} else
	        		if(files[0]==null) {
	        			selectedFileName.setText("No selection");	
	        		} else {
	        			selectedFileName.setText("Files selected");	
	        		}
	        	}
	        }
	    });		
	}

	public JPanel getPanel() {		
		setupPane();
		return selectionPanel;
	}
	
	public File[] getFiles() {
		if(!isMultipleSelectionsAllowed()) {
			System.out.println("Multiple selection not enabled, cannot retrieve files");
			return null;			
		} else if (files[0]==null){			
			System.out.println("No files selected");
			return null;
		} else {			
			return files;
		}
	}	
	
	public File getFile() {
		if(isMultipleSelectionsAllowed()) {
			System.out.println("Multiple selection is enabled, cannot retrieve single file");
			return null;
		} else if (files==null || files[0] == null){			
			System.out.println("No files selected");
			return null;
		} else {
			return files[0];
		}		
	}	
	
	private String getModalTitle() {
		String returnString = "Select file";
		if(getFileSelectionMode()==JFileChooser.DIRECTORIES_ONLY && isMultipleSelectionsAllowed()) {
			returnString = "Select directories";
		} else
		if(getFileSelectionMode()==JFileChooser.FILES_ONLY && isMultipleSelectionsAllowed()) {
			returnString = "Select files";
		} else
		if(getFileSelectionMode()==JFileChooser.DIRECTORIES_ONLY && !isMultipleSelectionsAllowed()) {
			returnString = "Select directory";
		}
		return returnString;
	}

	private String getFilterDescription() {
		String filterDescription;
		if(getExtensionFilter().toLowerCase().contains("xlsx")) {
			filterDescription = "Excel Files (*.xlsx)";
		} else if(getExtensionFilter()==null){
			filterDescription = null;
		} else {
			filterDescription = "Unknown";
		}			
		return filterDescription;
	}

	private String getExtensionFilter() {
		return extensionFilter;
	}

	private File getDefaultSelection() {
		return defaultSelection;
	}
	
	private boolean isMultipleSelectionsAllowed() {
		return multipleSelectionsAllowed;
	}

	private String getButtonText() {
		return buttonText;
	}
	
	private int getHorizontalCenter(int width) {
		return (dim.width-width)/2;
	}
	
	private int getVerticalCenter(int heigth) {
		return (dim.height-heigth)/2;
	}
	
}
