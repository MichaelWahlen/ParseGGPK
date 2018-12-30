package main.code.gui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class ViewOnGGPK extends JFrame
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTree tree;
    public ViewOnGGPK(DefaultMutableTreeNode root)
    {       
        tree = new JTree(root);
        JPanel mainPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setPreferredSize(new Dimension(1200,900));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollPane);
        this.add(mainPanel);      
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Folder structure \"Path of Exile\"");        
        this.pack();
        this.setVisible(true);
    }
    
    
}
