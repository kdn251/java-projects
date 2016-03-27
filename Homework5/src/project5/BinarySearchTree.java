package project5;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Represents a binary search tree using BSTNodes
 * @author Kevin Naughton Jr.
 *
 */

public class BinarySearchTree {
	
	//data fields for BST
	private BSTNode root; //store the root of the BST
	private int size; //store size of the BST
	private ArrayList<String> allWordsInOrder = new ArrayList<String>(); //store all words within text file
	
	
	/**
	 * Creates a BST by assigning given node to root of BST
	 * @param node - node to assign to root of BST
	 */
	BinarySearchTree(BSTNode node) {
		this.root = node; //set root of BST to given node
		size++; //increment size of BST
	}
	
	/**
	 * Creates an empty BinarySearchTree
	 */
	public BinarySearchTree() {
		this.root = null; //set root of BST to null
		size = 0; //set size
	}
	
	/**
	 * returns the size of the BST
	 * @return size - size of the BST
	 */
	public int getSize() {
		return this.size; //return size
	}
	
	/**
	 * returns the allWordsInOrder data field of the specific BST
	 * @return allWordsInOrder - ArrayList that when populated holds all words from text file
	 */
	public ArrayList getArraryList() {
		return this.allWordsInOrder; //return allWordsInOrder
	}
	
	/**
	 * @author Joanna Klukowska
	 * Add an item to this BST.
	 * @param item a new element to be added to the tree (ignored if null)
	 */
	public void insert(String itemToAdd) {
		if (itemToAdd != null) //check if itemToAdd is null
			root = recInsert(itemToAdd, root); //if the itemToAdd is not null call recInsert
	}
	
	/*
	 * @author Joanna Klukowska modified by Kevin Naughton Jr.
	 * Recursively add an item to this BST.
	 * @param item item to be added
	 * @param tree root of the subtree into which the node will be added
	 * @return reference to this BST after the item was inserted
	 */
	private BSTNode recInsert(String itemToAdd, BSTNode tree) {
		if (tree == null) {
			// Addition place found
			tree = new BSTNode(itemToAdd); //create new node storing itemToAdd
			size++; //increment size
		} else if (itemToAdd.compareTo(tree.getData()) < 0) //check if itemToAdd comes before current node's data alphabetically
			tree.setLeft(recInsert(itemToAdd, tree.getLeft())); // Add in left 
															// subtree
		else if(itemToAdd.compareTo(tree.getData()) > 0) //check if itemToAdd comes before current node's data alphabetically
			tree.setRight(recInsert(itemToAdd, tree.getRight())); // Add in right
																// subtree
		else
			tree.setCount(tree.getCount() + 1); //word already exists in BST so increment the count of the node
			
		return tree;
	}
	
	/**
	 * @author Joanna Klukowska modified by Kevin Naughton Jr.
	 * Remove the given item from this BST. If item is null or is not found
	 * in this BST, the three does not change.
	 * @param item an element to be removed.
	 */
	public void remove(String itemToRemove) {
		if (itemToRemove != null) //check if the itemToRemove is null
			root = recRemove(itemToRemove, root); //if the itemToRemove is not null call recRemove
	}
	
	/*
	 * @author Joanna Klukowska modified by Kevin Naughton Jr.
	 * Recursively remove an item from this BST.
	 * @param item  item to be removed
	 * @param tree  root of the subtree from which the item will be removed 
	 * @param cutoffValue number to compare each node's count to
	 * @return reference to this BST after the item was removed 
	 */
	private BSTNode recRemove(String itemToRemove, BSTNode tree) {

		if (tree == null)
			; // do nothing, item not found
		else if (itemToRemove.compareTo(tree.getData()) < 0) //check if itemToRemove comes before current node's data alphabetically
			tree.setLeft(recRemove(itemToRemove, tree.getLeft())); //recursive call on current node's left subtree
		else if (itemToRemove.compareTo(tree.getData()) > 0) //check if itemToRemove comes after current node's data alphabetically
			tree.setRight(recRemove(itemToRemove, tree.getRight())); //recursive call on current node's right subtree
		else {
				tree = removeNode(tree); //remove the node whose data is equal to itemToRemove
		}
		return tree;
	}
	
	/*
	 * @author Joanna Klukowska modified by Kevin Naughton Jr.
	 * Remove a particular node - the actual action depends on number of 
	 * childred that the node has. 
	 * @param tree the node to be removed 
	 * @return reference to this BST after node was removed 
	 */
	private BSTNode removeNode(BSTNode tree) {
		String data = tree.getData(); //store node's data

		//check is left subtree is null
		if (tree.getLeft() == null) {
			size--; //decrement size of BST
			return tree.getRight(); //return node's right subtree
		}
		//check if right subtree is null
		else if (tree.getRight() == null) {
			size--; //decrement size of BST
			return tree.getLeft(); //return node's left subtree
		}
		else {
			data = getPredecessor(tree.getLeft()); //find  predecessor (biggest number in BST that is still < current root)
			tree.setData(data); //set current node equal to predecessor
			tree.setLeft(recRemove(data, tree.getLeft())); //recursive call of current node's left subtree
			return tree;
		}
	}
	
	/**
	 * @author Joanna Klukowska
	 * Obtains the predecessor of a node (according to BST ordering). 
	 * @param tree node whose predecessor we are after
	 * @return the data contained in the predecessor node
	 */
	private String getPredecessor(BSTNode tree) {
		while (tree.getRight() != null) //traverse down right subtree as far as possible
			tree = tree.getRight(); //set current node to current node's right child
		return tree.getData(); //return data associated with rightmost node
	}

	/**
	 * 
	 * @param root - node to remove
	 * @param cutoffValue - lower bound for deciding whether or not a word should be removed
	 */
	private void removeWords(BSTNode root, int cutoffValue) {
		if(root != null) { //check if current node is null
			removeWords(root.getLeft(), cutoffValue); //recursive call on current node's left subtree
			removeWords(root.getRight(), cutoffValue); //recursive call on current node's right subtree
			if(root.getCount() < cutoffValue) { //check if current node's count is less than cutoffValue
				remove(root.getData()); //remove current node
			}
		}
	}
	
	/**
	 * helper method to call callRemoveWords
	 * @param cutoffValue - lower bound for deciding whether or not a word should be removed
	 */
	public void callRemoveWords(int cutoffValue) {
		removeWords(root, cutoffValue); //calls removeWords
	}
	
	
	

	
	
	
}
