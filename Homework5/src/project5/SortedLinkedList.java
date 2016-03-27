 package project5;
 
 /**
  * Represents a sorted linked list using SLLNodes
  * @author Kevin Naughton Jr.
  *
  */
public class SortedLinkedList {
	
	//DONT FORGET TO READ PROJECT 4 COMMENTS TO NOT LOSE POINTS!

	
	
	//data fields
	private SLLNode head; //points to front of list
	private int size; //stores size of list
	
	/**
	 * default constructor for SortedLinkedList
	 */
	SortedLinkedList() {
		 //default constructor
	}
	
	/**
	 * constructor for SortedLinkedList
	 * @param node - node to set to head of SortedLinkedList
	 */
	SortedLinkedList(SLLNode node) {
		this.head = node; //set head to node
		size++; //increment size of SortedLinkedList
	}
	
	/**
	 * returns the size of the SortedLinkedList
	 * @return size - size of SortedLinkedList
	 */
	public int getSize() {
		return this.size; //return size
	}
	
	/**
	 * returns the reference to a node that stores a specific piece of data
	 * @param stringToFind - data to find
	 * @return current - references node that stores stringToFind
	 */
	public SLLNode get(String stringToFind) {
		
		SLLNode current = head; //set current to head
		while(current.getData().compareTo(stringToFind) != 0) { //continue while current node's data does not equal stringToFind
			if(current.getNext() == null) { //account for last node in the SortedLinkedList
				if(current.getData().compareTo(stringToFind) != 0) { //check if last node in SortedLinkedList's data is equal to stringToFind
					return null; //return
				}
			}
			current = current.getNext(); //set current equal to the next node in the SortedLinkedList
			
		}
		return current; //return the node that stores stringToFind
	}
	
	/**
	 * adds an item to the SortedLinkedList if it does not already exist
	 * @param itemToAdd - item to add to SortedLinkedList
	 */
	public void enqueue(String itemToAdd) {
		//check if list is empty
		if(this.head == null) {
			this.head = new SLLNode(itemToAdd); //point head of list to new item
			size++; //increment size of the list
		}
		//store what is returned by get method in nodeToIncrement
		SLLNode nodeToIncrement = get(itemToAdd);
		
		//check if nodeToIncrement is not null
		if(nodeToIncrement != null) {
			nodeToIncrement.setCount(nodeToIncrement.getCount() + 1); //increment the count of nodeToIncrement by 1
		}
		//check if nodeToIncrement is null
		if(nodeToIncrement == null) {
			//create node to add to list
			SLLNode newNode = new SLLNode(itemToAdd);
			
			//create current node to loop through list
			SLLNode current = head;
			
			//instantiate pointToCurrent
			SLLNode oneNodeBehindCurrent = null;
			
			//continue along the list while data of newNode > data of current node
			while(!(newNode.getData().compareTo(current.getData()) < 0)) {
				//keep pointToCurrent one node behind current
				oneNodeBehindCurrent = current;
				//point current to next node in the list
				current = current.getNext();
			}
			if(oneNodeBehindCurrent == null) { //check if oneNodeBehindCurrent is null
				newNode.setNext(head); //point newNode to head of SortedLinkedList
				head = newNode; //set head to newNode
				size++; //increment size
			}
			else {//set oneNodeBehindCurrent's next to newNode
			oneNodeBehindCurrent.setNext(newNode);
			//point newNode to node after current
			newNode.setNext(current);

			//increment size of list
			size++;
			}
		}
	}
	
	/**
	 * prints all elements in SortedLinkedList
	 */
	public String printList() {
		String data = "";
		SLLNode current = head; //set current equal to head
		while(current != null) { //continue while current node is not null
			data = data + current.getCount() + "     " + current.getData() + "\n"; //print out current node's count and data
			current = current.getNext(); //advance current to next node in SortedLinkedList
		}
		return data;
	}

	/**
	 * Removes a node from the SortedLinkedList if its count is less than cutoffValue
	 * @param cutoffValue - lower bound for deciding whether or not a node should be removed from SortedLinkedList
	 */
	public void remove(int cutoffValue) {
		//check if the linked list is empty
		if(this.head == null) {
			return;
		}
		SLLNode current = head; //point current to front of list
		
		SLLNode oneNodeBehindCurrent = null; //point oneNodeBehindCurrent to null
		
		//check if head's count < cutoffValue and the linked list has more than 1 element
		if(current.getCount() < cutoffValue && current.getNext() != null) {
			head = head.getNext(); //point head to next node in linked list
			current = head; //point current to the new head reference
			size--;
		}
		//check if head's count < cutoffValue and linked list only has 1 element (head)
		else if(current.getCount() < cutoffValue && current.getNext() == null) {
			this.head = null; //remove linked list
		}
		while(current.getNext() != null) { //continue while there is another node past the current node
			oneNodeBehindCurrent = current;
			current = current.getNext();
			//check if current node's count < cutoffValue
			if(current.getCount() < cutoffValue) { //check if current's count is less than cutoffValue
				oneNodeBehindCurrent.setNext(current.getNext()); //set oneNodeBehindCurrent's next to current's next
				current = oneNodeBehindCurrent; //set current to oneNodeBehindCurrent
				size--; //decrement size
			}
		}
		if(current.getCount() < cutoffValue) { //account for last node in SortedLinkedList and check if it's count is less than cutoffValue
			current = null; //remove last node in SortedLinkedList
			size--; //decrement size
		}
	}
	
	
}
