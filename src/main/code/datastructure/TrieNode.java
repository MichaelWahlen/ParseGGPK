package main.code.datastructure;

import java.util.List;

public interface TrieNode<T> {
	/**
	 * Method used to insert the characters into the TieTree. Position indicates where in the array the insertion process currently is.
	 * This allows to use a single char[] object without having to create additional String or char[] object.
	 * @param characters the characters that need to be inserted
	 * @param position used to measure where in the array the insertion process is
	 */
	public void insertKey(char[] characters, int position);
	/**
	 * Method used to insert the characters into the TieTree. Position indicates where in the array the insertion process currently is.
	 * This allows to use a single char[] object without having to create additional String or char[] object. 
	 * Additionally at the end node an object is attached.
	 * @param characters the characters that need to be inserted
	 * @param position used to measure where in the array the insertion process is
	 * @param object the object that needs to be attached at the end point of char array
	 */
	public void insertKey(char[] characters, int position, T object);
	/**
	 * Locates an object (no guarantee on existence), located at the end of the char[] key in the Tree. 
	 * If the key char[] does not exist in the tree, null is returned.
	 * @param key the key char array which determines where the object is expected to be located
	 * @param position used to measure where in the array the search process is
	 * @return returns the object located at the end node of the character key, or null if not present.
	 */
	public T findObject(char[] key, int position);
	/**
	 * Returns the object at the node
	 * @return the object at this node, or null if no object is set
	 */
	public T getObject();	
	/**
	 * Indicates whether this node is an end point of an inserted key. 
	 * Does not imply that the node is a leaf of the tree,as later additions might follow this path through the tree.
	 * @param keyMarker
	 */
	public void setEndMarker(boolean keyMarker);
	/**
	 * Attaches an object to this node.
	 * @param object the object to attach
	 */
	public void setObject(T object);
	/**
	 * Used to find all keys that start with the provided prefix, and add them to the provided holder list.
	 * @param prefix the prefix that the keys need to start with	
	 * @param position the position used to travel to the end of the prefix
	 * @param holderList the list used to add keys with the required prefix to
	 */
	public void findPrefixedKeys(String prefix, int position, List<String> holderList);
	/**
	 * A helper method for the findPrefixedKeys
	 * @param prefix the prefix string
	 * @param holderList the list used to add keys with the required prefix to
	 */
	public void findStringsAfterPrefix(String prefix, List<String> holderList);
	/**
	 * Verify whether the nod indicates that it's the end char for a key
	 * @return
	 */
	public boolean hasEndMarker();
	
	
}
