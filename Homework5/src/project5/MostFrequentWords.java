package project5;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Computes a list of unique words given a text file
 * stores words in both a SortedLinkedList and a BinarySearchTree
 * Prunes both structures with a user based cutoff value
 * compares relative performance of each structure 
 * and prints remaining elements of LinkedList after pruning to an output file
 * @author Kevin Naughton Jr.
 */
public class MostFrequentWords {
	
	/**
	 * driver of the program
	 * @param args - 3 command line arguments
	 */
	public static void main(String[] args) {
		//check if three arguments are provided on the command line
		if(args.length != 3) {
			System.out.println("3 arguments are required for this program to run."); //print error message
			System.exit(1); //exit program gracefully
		}
		
		//create file based on first command line argument
		File fileToRead = new File(args[0]);
		
		//check if file given by first command line argument  exists
		if(!fileToRead.exists()) {
			System.out.println("Could not find the specified input file."); //print error message
			System.exit(1); //exit program gracefully
		}
		
		//check if program can read from the file given by the first command line argument
		if(!fileToRead.canRead()) {
			System.out.println("Cannot read specified input file."); //print error message
			System.exit(1); //exit program gracefully
		}
		
		//initialize cutoffValue
		int cutoffValue = 0;
		
		//validate the second command line argument
		try {
			//convert second command line argument to integer
			cutoffValue = Integer.parseInt(args[1]); //stores cutoff value of the words that should be printed to the output file
			//Ensure the second command line argument is greater than or equal to zero
			if(cutoffValue < 0) {
				System.out.println("Second command line argument must be an integer greater than or equal to zero."); //print error message
				System.exit(1); //exit program gracefully
			}
		}
		//catch any exception an invalid second command argument may cause
		catch(Exception E) {
			System.out.println("Second argument on the command line must be a positive integer."); //print error message
			System.exit(1); //exit program gracefully	
		}
		
		//create output file based on third command line argument
		File outputFile = new File(args[2]);
		
		//initialize writer
		PrintWriter writer = null;
		
		//validate the third command line argument
		try {	
			//create PrintWriter object to write to output file
			writer = new PrintWriter(outputFile);
		}
		//catch any exception an invalid third command line argument may cause
		catch(Exception e) {
			System.out.println("Output file could not be found."); //print error message
			System.exit(1); //exit program gracefully
		}
		
		//read in file from first command line argument
		
		//instantiate FileParser object
		FileParser textFile;
		
		//create ArrayList to hold all words from text file given by the first command line argument
		ArrayList<String> allWords = new ArrayList<String>();
		
		//start time variable for reading file
		long startReadTime = System.nanoTime();
		
		//try to create FileParser object
		try {
			textFile = new FileParser(args[0]); //set textFile equal to the first command line argument
			allWords = textFile.getAllWords(); //populate allWords with all words in given text file
		} 
		//catch IOException
		catch (IOException e) { 
			System.out.println(e); //print error message to user
			System.exit(1); //exit program gracefully
		}
		
		//end time variable for reading file
		long endReadTime = System.nanoTime();
		
		//create a linked list to store all words in text file
		SortedLinkedList linkedList = new SortedLinkedList();
	
		//start time variable for linked list
		long startTimeLinkedList = System.nanoTime();
		
		//iterate through allWords
		for(int i = 0; i < allWords.size(); i++) {
			//create new node to add to linked list
			SLLNode node = new SLLNode(allWords.get(i));
			//add new node to linked list
			linkedList.enqueue(node.getData());
		}
		
		//end time variable for linked list
		long endTimeLinkedList = System.nanoTime();
		
		//store number of words in linked list before pruning
		int sizeBeforePruningLL = linkedList.getSize();
		
		//start time variable for linked list pruning
		long startTimeLinkedListPruning = System.nanoTime();
	
		//prune linked list to remove all words with count lower than cutoffValue
		linkedList.remove(cutoffValue);

		//end time variable for linked list pruning
		long endTimeLinkedListPruning = System.nanoTime();
				
		//create a binary search tree to store all words in text file
		BinarySearchTree BST = new BinarySearchTree();
		
		//start time variable for binary search tree
		long startTimeBinarySearchTree = System.nanoTime();
		
		for(int i = 0; i < allWords.size(); i++) {
			//create a new BSTNode to add to binary search tree
			BSTNode bstNode = new BSTNode(allWords.get(i));
			//add new BSTNode to binary search tree
			BST.insert(bstNode.getData());
		}
		
		//end time variable binary search tree		
		long endTimeBinarySearchTree = System.nanoTime();

		//store number of words in binary search tree before pruning
		int sizeBeforePruningBST = BST.getSize();
		
		//start time variable for binary search tree pruning
		long startTimeBinarySearchTreePruning = System.nanoTime();
	
		//prune binary search tree
		BST.callRemoveWords(cutoffValue);
		
		//end time variable for binary search tree pruning
		long endTimeBinarySearchTreePruning = System.nanoTime();
		
		//processing information for reading file
		System.out.println("INFO: Reading file took " + (endReadTime - startReadTime) + " ms " + "(~     " + ((endReadTime - startReadTime) / 1000000000) + " seconds).");
		System.out.println("INFO: " + allWords.size() + " words read.");
		
		//print line break in between processing information of reading file and processing information of linked list
		System.out.println();

		//processing information for linked list
		System.out.println("Processing using Sorted Linked List");
		System.out.println("INFO: Creating index took " + (endTimeLinkedList - startTimeLinkedList) + " ms " + "(~     " + ((endTimeLinkedList - startTimeLinkedList) / 1000000000) + " seconds).");
		System.out.println("INFO: " + sizeBeforePruningLL + " words stored in the index.");
		System.out.println("INFO: Pruning index took " + (endTimeLinkedListPruning - startTimeLinkedListPruning) + " ms " + "(~     " + ((endTimeLinkedListPruning - startTimeLinkedListPruning) / 1000000000) + " seconds).");
		System.out.println("INFO: " + linkedList.getSize() + " words remaining after pruning.");
		
		//print line break in between the processing information for each data structure
		System.out.println();
				
		//processing information for binary search tree
		System.out.println("Processing using Recursive BST");
		System.out.println("INFO: Creating index took " + (endTimeBinarySearchTree - startTimeBinarySearchTree) + " ms " + "(~     " + ((endTimeBinarySearchTree - startTimeBinarySearchTree) / 1000000000) + " seconds).");
		System.out.println("INFO: " + sizeBeforePruningBST + " words stored in index.");
		System.out.println("INFO: Pruning index took " + (endTimeBinarySearchTreePruning - startTimeBinarySearchTreePruning) + " ms " + "(~     " + ((endTimeBinarySearchTreePruning - startTimeBinarySearchTreePruning) / 1000000000) + " seconds).");
		System.out.println("INFO: " + BST.getSize() + " words remaining after pruning.");
		
		writer.println(linkedList.printList()); //print all words remaining in linked list after pruning to output file
		writer.close(); //close writer
				
	}

}
