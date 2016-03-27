package project3;

public class SelectionSort <E extends Comparable <E> > implements Sorter<E> {

	/**
	 * Sorts the given array using the selection sort algorithm.
	 * @param list array to sort.
	 */
	public void sort(E[] list) {
		//loop through list
		for(int i = 0; i < list.length - 1; i++) {
			//maintain current index within first loop
			int currentPosition = i;
			//loop through list again to compare k to currentPosition
			for(int k = i + 1; k < list.length; k++) {
				//check if k is less than currentPosition
				if(list[k].compareTo(list[currentPosition]) < 0) {
					//switch currentPosition to point to k
					currentPosition = k;
				}
			}
				//swap smaller element with larger element
				E smaller = list[currentPosition];
				
				list[currentPosition] = list[i];
				
				list[i] = smaller;
			
		}
	}

}
