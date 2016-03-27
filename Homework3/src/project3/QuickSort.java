package project3;

public class QuickSort <E extends Comparable <E> > implements Sorter<E> {

	private E copyArray[];
	private int length;
	
	/**
	 * Sorts the given array using the quick sort algorithm.
	 * @param list array to sort.
	 */
	public void sort(E[] list) {
		//copy list
		copyArray = list;
		//store length of list
		length = list.length;
		
		//start quick sort
		sort(0, length - 1);
	}
	/*
	 * performs selection sort
	 * @param low - starting point of the array to sort
	 * @param high - ending point of the array to sort
	 */
	private void sort(int low, int high ) {
		int i = low; //store starting point
		int k = high; //store ending point
		
		//select pivot
		//E pivot = copyArray[low];
		
		//IMPROVEMENT: set pivot to middle element of array
		E pivot = copyArray[low + (high - low) / 2];
		
		
		while (i <= k) {
			//increment i while i < pivot
			while (copyArray[i].compareTo(pivot) < 0) {
				i++;
			}
			//decrement k while k > pivot
			while (copyArray[k].compareTo(pivot) > 0) {
				k--;
			}
			//place number on the correct side of the pivot
			if (i <= k) {
				swap(i, k);
				i++; //advance pointer to next index
				k--; //advance pointer to next index
			}
		}
		
		//call sort method again (recursive call)
		if (low < k) {
			sort(low, k);
		}
		if (i < high) {
			sort(i, high);
		}
		
	}
	/*
	 * swaps index positions i and k
	 * @param i - number to swap with k
	 * @param k - number to swap with i
	 */
	private void swap(int i, int k) {
		E temp = copyArray[i];
		copyArray[i] = copyArray[k];
		copyArray[k] = temp;
	}

}
