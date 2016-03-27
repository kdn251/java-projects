package proj4;

/**
 * 
 * @author Kevin Naughton Jr.
 * Represents a queue and a queue's methods by implementing a Node class
 * @param <E> Generic type - represents a generic type
 */
public class NewQueue<E> {
	//create data fields for the queue
	private Node head; //points to the front of the queue
	private Node tail; //points to the back of the queue
	private int size; //stores the size of the queue
	
	/**
	 * Constructor for queues, creates a new queue object
	 */
	public NewQueue() {
		this.head = null; //sets the head of the queue to null
		this.tail = null; //sets the tail of the queue to null
		int size = 0; //sets the size of the queue to null
	}
	
	/**
	 * Tests if this queue is empty
	 * @return true if this queue object is empty or false if this queue object is not empty
	 */
	public boolean empty() {
		if(head == null) { //checks if this queue is empty
			return true; //this queue is empty
		}
		return false; //this queue is not empty
	}
	
	/**
	 * Returns the element at the front of this queue without removing it from the queue
	 * @return returnData - the information that the head (front) of the queue holds or null if this queue is empty
	 */
	public E peek() {
		if(head == null) { //checks if this queue is empty
			return null;
		}
		E returnData = (E)head.getData(); //store head (front) of this queue's value in returnData
		return returnData;
	}
	
	/**
	 * Removes the element at the front of this queue and returns that element as the value of this function
	 * @return returnData - the information that the head (front) of the queue holds or null if this queue is empty
	 */
	public E dequeue() {
		if(head == null) { //checks if this queue is empty
			return null; //this queue is empty
		}
		E returnData = (E)head.getData(); //store data to return
		head = head.getNext(); //advance the head of the queue to the next element
		size--; //decrement size of queue
		
		return returnData; //return front element's value
	}
	
	/**
	 * Adds an item to the back of this queue. Returns the item itself
	 * @param item - item to be enqueued to the queue
	 * @return the item that was enqueued to the queue
	 */
	public E enqueue(E item) {
		Node addedItem = new Node(item); //create new node and store the item to add to the queue within it
		if(head == null) { //checks if this queue is empty
			head = addedItem; //sets item to be enqueued to the head of the queue
			tail = addedItem; //sets item to be enqueued to the tail of the queue
			size++; //increment size of the queue
		}
		else {
			tail.setNext(addedItem); //sets the item to be enqueued to the reference of the tail of the queue
			size++; //increment size of the queue
			tail = addedItem; //set tail to the item added to the queue
		
		}
		
		return item; //return item that was enqueued to the queue

	}
}
