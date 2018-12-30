package main.code.parse;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.tree.DefaultMutableTreeNode;

public class InnerNode{
	
	private Map<String, InnerNode> childNodes = new TreeMap<String, InnerNode>();
	private DefaultMutableTreeNode jTreeNode;
	
	public InnerNode(List<List<String>> stringLists) {
		jTreeNode = new DefaultMutableTreeNode("Test");
		for(List<String> stringList: stringLists) {
			add(stringList);				
		}			
	}
	
	private InnerNode() {
		
	}
	
	private void createJTreeNode(String nodeName) {
		jTreeNode = new DefaultMutableTreeNode(nodeName);
	}

	private void add(List<String> strings) {
		if(strings.size()>0) {
			String selectedString = strings.get(0);
			InnerNode child = childNodes.get(selectedString);
			strings.remove(0);
			if(child==null) {
				child = new InnerNode();
				child.createJTreeNode(selectedString);
				addToParentJNode(child.getJTreeNode());
				childNodes.put(selectedString, child);
			} 
			child.add(strings);						
		}			
	}
	
	private void addToParentJNode(DefaultMutableTreeNode childNode) {
		jTreeNode.add(childNode);
	}
	
	private DefaultMutableTreeNode getJTreeNode() {
		return jTreeNode;
	}
	
	public DefaultMutableTreeNode getTopLevelJTreeNode() {
		return jTreeNode;
	}
	
}
