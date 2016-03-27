package project3;

/**
 * The interface for the implementation of different sort algorithms. 
 * 
 * @author Joanna Klukowska
 *
 * @param <E> The type of objects that this Sorter objects expects in
 *                       the list to be sorted. 
 */
interface Sorter <E extends Comparable <E> >{
	
	/**
	 * Sorts the list according to the natural ordering of its elements
	 * from smallest to largest. The list is modified. 
	 * @param list array of elements to be sorted
	 */
	public void sort(  E[] list );
}
