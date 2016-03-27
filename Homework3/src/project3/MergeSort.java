package project3;

public class MergeSort <E extends Comparable <E> > implements Sorter<E> {
	//holds copy of array to sort
	private E[] listCopy;
	//stores length of arary to sort
	private int length;
	//stores sorted elements of the array to sort
	private E[] mergeArray;
	
	/**
	 * Sorts the given array using the  merge sort algorithm.
	 * @param list array to sort.
	 */
	public void sort(E[] list) {
		//copy the array to be sorted
		listCopy = list;
		//store the length of the array
		length = list.length;
		//copy the array to be sorted
		mergeArray = list;
		
		//initiate merge sort
		sort(0, length - 1);
	}
	
	/*
	 * sorts array by continuously splitting array into sorted subarrays
	 * @param low - lower bound of array to be sorted
	 * @param high - upper bound of array to be sorted
	 */
	private void sort(int low, int high) {
		if(low < high) {
			//store the middle element of the array
			int middleIndex = low + (high - low) / 2;
			
			//sort the left side of the array
			sort(low, middleIndex);
			
			//sort the right side of the array
			sort(middleIndex + 1, high);
			
			//combine the two sorted sides of the array
			combine(low, middleIndex, high);
		}
	}
	
	/*
	 * combines to the now two sorted halves of the array
	 * @param low - start of array to combine
	 * @param middleIndex - middle index of array to combine
	 * @param high - end of array to combine
	 */
	private void combine(int low, int middleIndex, int high) {
		//copy elements from listCopy to mergeArray
		for(int i = low; i <= high; i++) {
			mergeArray[i] = listCopy[i]; //populates mergeArray
		}
		//store values
		int i = low;
		int j = middleIndex + 1;
		int k = low;
		
		
		while(i <= middleIndex && j <= high) {
			if(mergeArray[i].compareTo(mergeArray[j]) < 0) {
				//copy lower element to listCopy if current index < middle index
				listCopy[k] = mergeArray[i];
			}
			else {
				//otherwise set start of array to the middle index
				listCopy[k] = mergeArray[j];
				j++; //increment j
			}
			k++; //increment k
		}
		
		//copy sorted elements to listCopy
		while(i <= middleIndex) {
			listCopy[k] = mergeArray[i];
			k++; //increment k
			i++; //increment i
		}
	}
	
		

}
