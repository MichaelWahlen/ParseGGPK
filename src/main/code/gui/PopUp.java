package main.code.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PopUp {
	
	private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	private final JFrame parentFrame;
	
	public PopUp(JFrame parentFrame) {
		this.parentFrame = parentFrame;
	}

	public void openJDialogue(String message) {
		JDialog dialog = new JDialog();	
		JPanel overviewPanel = new JPanel(new GridLayout(0,1));						
		JButton closeButton = new JButton("Close");		   			
		closeButton.addActionListener(new ActionListener()	{
	        @Override
	        public void actionPerformed(ActionEvent e) {			        	
	        	dialog.dispose();
	        	parentFrame.setVisible(true);
	        }
		});		
		overviewPanel.add(new JLabel(message));
		overviewPanel.add(closeButton);	
		dialog.add(overviewPanel);
		dialog.pack();
		dialog.setLocation(getHorizontalCenter(dialog.getWidth()), getVerticalCenter(dialog.getHeight())); 		
		parentFrame.setVisible(false);
		dialog.setVisible(true);
	}
	
	private int getHorizontalCenter(int width) {
		return (dim.width-width)/2;
	}
	
	private int getVerticalCenter(int heigth) {
		return (dim.height-heigth)/2;
	}
}
