package proj4;

/**
 * 
 * @author Kevin Naughton Jr.
 * Used to implement abstract data types, specifically, stacks and queues
 * @param <E> Generic type - represents a generic type
 */
public class Node<E> {
	//create data fields for Node
	private E data; //used to store data in the node
	private Node next; //used to point to the next node
	
	/**
	 * Constructor for nodes, creates a node object
	 * @param item - item to store in a specific node
	 */
	public Node(E item) {
		this.data = item; //set current node's data data field to item
	}
	
	/**
	 * Gets the data associated with a specific node
	 * @return returns the data of a specific node
	 */
	public E getData() {
		return this.data;
	}
	
	/**
	 * Gets the reference to the next node
	 * @return returns the reference to the next node
	 */
	public Node getNext() {
		return this.next;
	}
	
	/**
	 * Stores given information in the data data field of a specific node
	 * @param newData - represents the data to store inside the specific node
	 */
	public void setData(E newData) {
		this.data = newData; //sets current node's data data field to newData
	}
	
	/**
	 * Stores a new given reference to another node in the next data field of a specific node
	 * @param newNext - represents the new reference to the next node to store
	 */
	public void setNext(Node newNext) {
		this.next = newNext; //sets current node's next data field to newNext
	}
	
}
