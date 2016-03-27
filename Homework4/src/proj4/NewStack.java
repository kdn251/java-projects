package proj4;

/**
 * 
 * @author Kevin Naughton Jr.
 *Represents a stack and a stack's methods by implementing a Node class
 * @param <E> Generic type - represents a generic type
 */
public class NewStack<E> {
	//create data fields for stack
	private Node head; //points to the top of the stack
	private int size; // stores the size of the stack
	
	/**
	 * Constructor for stacks, creates a new stack object
	 */
	public NewStack() {
		this.head = null; //sets the head of the stack to null
		this.size = 0; //sets the size of the stack to zero
	}
	
	/**
	 * Tests if this stack is empty
	 * @return true if this stack object is empty or false if this stack object is not empty
	 */
	public boolean empty() {
		if(this.head == null) { //checks if this stack is empty
			return true; //this stack is empty
		}
		return false; //this stack is not empty
	}
	
	/**
	 * Removes the element at the top of this stack and returns that element as the value of this function
	 * @return the top element's value or null if the stack object is empty
	 */
	public E pop() {
		Node current = head; //sets current to head
		if(head == null) { //checks if this stack is empty
			return null; //this stack is empty
		}
		E returnData; //create variable to store top element's value in
		returnData = (E) head.getData(); //stores data to return
		
		head = head.getNext(); //advance head of the stack to the next element
		size--; //decrement size of stack
		
		return returnData; //return top element's value
		
	
	}

	/**
	 * Pushes an item onto the top of this stack. Returns the item itself
	 * @param item - item to be pushed onto the top of this stack
	 * @return the item that was pushed onto the top of this stack
	 */
	public E push(E item) {
		Node newItem = new Node(item); //create new node and store the item to add to the stack within it
		newItem.setNext(head); //set reference of the new node to the head (top) of this stack
		head = newItem; //point head (top) of this stack to the new item
		size++; //increment size because we are adding a new item to this stack
		return item; //return the item that was added to this stack
		
	}
	
	/**
	 * Returns the element at the top of this stack without removing it from the stack
	 * @return returnData - the information that the head (top) of the stack holds or null if this stack is empty
	 */
	public E peek() {
		if(head == null) { //checks if this stack is empty
			return null; //this stack is empty
		}
		else {
			E returnData = (E)head.getData(); //store head (top) of this stack's value in returnData
			return returnData;

		}
		
	}
	
	

}
