package project3;

import java.util.Arrays;
import java.util.Random;

/**
 * The benchmark used for evaluation of sorting method. 
 * 
 * When RUN_ALL is set to false, it runs the merge sort, quick sort and 
 * selection sort on an array of 10,000 integers and displays the time it took
 * for each to complete. Times displayed in microseconds. 
 * 
 * When RUN_ALL is set to true, it performs two "experiments":
 * 1) runs merge sort, quick sort and selection sort on arrays of integers
 * ranging in size from 10,000 to 100,000 in increments of 10,000
 * 2) runs merge sort and quick sort on arrays of integers ranging in size
 * from 50,000 to 1,000,000 in increments of 50,000. 
 * Times are displayed in microseconds.   
 * 
 * @author Joanna Klukowska
 *
 */
public class SortTester {
	static private Random rand = new Random(0);
	
	//TODO: set this to false before submitting your code
	//set it to true to collect the results 
	private static final boolean RUN_ALL = false;
	
	public static void main(String[] args) {
		
		//////////////////////////////////////////////
		//run initial short runs
		//////////////////////////////////////////////
		int arraySize = 10000; 
		
		Integer[] list = generateRandomIntegerArray(arraySize);
		Sorter<Integer> arrayIntSorter;
		System.out.printf("\nArray size: %d", arraySize);
		
		System.out.printf("\nRunning merge sort: ");
		arrayIntSorter = new MergeSort<Integer>();
		runTest(arrayIntSorter, list);

		System.out.printf("\nRunning quick sort: ");
		arrayIntSorter = new QuickSort<Integer>();
		runTest(arrayIntSorter, list);

		System.out.printf("\nRunning selection sort: ");
		arrayIntSorter = new SelectionSort<Integer>();
		runTest(arrayIntSorter, list);
		
		
		if (  !RUN_ALL ) { 
			System.exit(0);
		}
		////////////////////////////////////////////////	
		//run all three algorithms
		////////////////////////////////////////////////
		System.out.printf("\n\n%15s\t%15s\t%15s\t%15s", "N    ", "Merge", "Quick", "Selection");
		for ( arraySize = 10000; arraySize <= 100000; arraySize = arraySize + 10000) {
			
			list = generateRandomIntegerArray(arraySize);
			System.out.printf("\n%15d", arraySize);
			
			arrayIntSorter = new MergeSort<Integer>();
			runTest(arrayIntSorter, list);
			
			arrayIntSorter = new QuickSort<Integer>();
			runTest(arrayIntSorter, list);
			
			arrayIntSorter = new SelectionSort<Integer>();
			runTest(arrayIntSorter, list);
		}
		

		////////////////////////////////////////////////
		//run two "fast" algorithms
		////////////////////////////////////////////////
		System.out.printf("\n\n%15s\t%15s\t%15s", "N    ", "Merge", "Quick" );
		for ( arraySize = 50000; arraySize <= 1000000; arraySize = arraySize + 50000) {
			
			list = generateRandomIntegerArray(arraySize);
			System.out.printf("\n%15d", arraySize);
			
			arrayIntSorter = new MergeSort<Integer>();
			runTest(arrayIntSorter, list);
			
			arrayIntSorter = new QuickSort<Integer>();
			runTest(arrayIntSorter, list);
		}

	}

	/** 
	 * Run a particular sort algorithm on the given list.
	 * @param sorter an object representing one of the sort methods for arrays 
	 * @param list array to be used for sort timing, the array is not modified 
	 */
	public static <E extends Comparable<E>> void runTest(Sorter<E> sorter, 	E[] list) {

		long start, end;

		// create a copy of the array to be sorted
		E listCopy[] = Arrays.copyOf(list, list.length);
		
		//invoke the garbage collector
		System.gc();
		// start the timer
		start = System.nanoTime();
		sorter.sort(listCopy);
		end = System.nanoTime();
		// verify that the array is sorted
		if (!isSorted(listCopy))
			System.err.printf("Not sorted - problems in %s", sorter.getClass() );
		System.out.printf("\t%15d", (end - start) / 1000);

	}

	/**
	 * Determines if a given list is sorted.
	 * @param list array containing data 
	 * @return true, if list is in a sorted order (according to the natural ordering of its elements), 
	 *              false, otherwise
	 */
	public static <E extends Comparable<E>> boolean isSorted(E[] list) {
		for (int i = 1; i < list.length; i++) {
			if (list[i - 1].compareTo(list[i]) > 0) {
				return false;
			}
		}
		return true;
	}

	/** Generates an array of random integers in the range of valid integer values. 
	 * @param size size of the array to generate
	 * @return an array of random integers
	 */
	public static Integer[] generateRandomIntegerArray(int size) {
		Integer list[] = new Integer[size];

		for (int i = 0; i < list.length; i++)
			list[i] = rand.nextInt(); 
		return list;
	}

	
}
