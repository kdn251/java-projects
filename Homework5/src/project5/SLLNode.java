package project5;

/**
 * Used to implement a sorted linked list
 * @author Kevin Naughton Jr.
 *
 */
public class SLLNode implements Comparable<SLLNode> {
	//data fields for each individual SLLNode
	private String data; //store the data of the SLLNode
	private SLLNode next; //store the next reference of the SLLNode
	private int count = 0; //store the count of the SLLNode
	
	/**
	 * SLLNode constructor - creates SLLNode given a string
	 * @param item - data to store in current SLLNode's data
	 */
	public SLLNode(String item) {
		this.data = item; //set current SLLNode's data to item
		count++; //increment count
	}
	
	/**
	 * returns current SLLNode's data
	 * @return current SLLNode's data
	 */
	public String getData() {
		return this.data; //return data
	}
	
	/**
	 * returns current SLLNode's next reference
	 * @return current SLLNode's next reference
	 */
	public SLLNode getNext() {
		return this.next; //return next
	}
	
	/**
	 * sets current SLLNode's data to newData
	 * @param newData - new String to store in current SLLNode's data
	 */
	public void setData(String newData) {
		this.data = newData; //set current SLLNode's data to newData
	}
	
	/**
	 * points current SLLNode's next reference to a new SLLnode
	 * @param newNext - new next reference to store in current SLLNode's next
	 */
	public void setNext(SLLNode newNext) {
		this.next = newNext; //set current SLLNode's next to newNext
	}
	
	/**
	 * returns current SLLNode's count
	 * @return count of current SLLNode
	 */
	public int getCount() {
		return this.count; //return count
	}
	
	/**
	 * sets current SLLNode's count to newCount
	 * @param newCount - new count to assign to current SLLNode's count
	 */
	public void setCount(int newCount) {
		this.count = newCount; //set current SLLNode's count to newCount
	}

	@Override
	/**
	 * overrides default to string method
	 * compares SLLNodes based on their data
	 */
	public int compareTo(SLLNode arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
