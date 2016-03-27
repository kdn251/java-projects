package project5;

/**
 * Used to implement a binary search tree
 * @author Kevin Naughton Jr.
 *
 */
public class BSTNode implements Comparable<BSTNode> {	
	
	//data fields for each individual BSTNode
	private String data; //store the data of the BSTNode
	private BSTNode left; //store the left reference of the BSTNode
	private BSTNode right; //store the right reference of the BSTNode
	private int count = 0; //store the count of the BSTNode
	
	/**
	 * Creates a BSTNode given a String
	 * @param item - item to store in BSTNode
	 */
	public BSTNode(String item) {
		this.data = item; //set BSTNode's data to item
		this.left = null; //set BSTNode's left to null
		this.right = null; //set BSTNode's right to null
		count++; //increment count
	}
	
	/**
	 * returns current BSTNode's data
	 * @return data - current BSTNode's data
	 */
	public String getData() {
		return this.data; //return data
	}
	
	/**
	 * returns current BSTNode's left reference
	 * @return left - current BSTNode's left reference
	 */
	public BSTNode getLeft() {
		return this.left; //return left
	}
	
	/**
	 * returns current BSTNode's right reference
	 * @return right - current BSTNode's right reference
	 */
	public BSTNode getRight() {
		return this.right; //return right
	}
	
	/**
	 * Sets current BSTNode's data to newData
	 * @param newData - data to set to current BSTNode's data
	 */
	public void setData(String newData) {
		this.data = newData; //set current BSTNodes data to newData
	}
	
	/**
	 * Sets current BSTNode's left to newLeft
	 * @param newLeft - new left reference to set to current BSTNode's left
	 */
	public void setLeft(BSTNode newLeft) {
		this.left = newLeft; //set current BSTNode's left reference to newLeft
	}
	
	/**
	 * Sets current BSTNode's right to newRight
	 * @param newLeft - new right reference to set to current BSTNode's right
	 */
	public void setRight(BSTNode newRight) {
		this.right = newRight; //set current BSTNode's right reference to newRight
	}
	
	/**
	 * returns current BSTNode's count
	 * @return count - current BSTNode's count
	 */
	public int getCount() {
		return this.count; //return count
	}
	
	/**
	 * sets current BSTNode's count to newCount
	 * @param newCount - new count to be assigned to current BSTNode's count
	 */
	public void setCount(int newCount) {
		this.count = newCount; //sets current BSTNode's count to newCount
	}

	@Override
	/**
	 * overrides default toString method
	 * compares nodes based on their data
	 */
	public int compareTo(BSTNode o) {
		// TODO Auto-generated method stub
		return 0;
	}
	

	
	
	
	
	
	
	
}
