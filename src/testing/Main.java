package testing;
import java.util.Arrays;
import java.util.Random;

import sorters.BubbleSort;
import sorters.QuickSort;

public class Main {

	public static void main(String[] args) {
		//This class was just for testing.
		BubbleSort bubbleSort = new BubbleSort();
		SortEventListener bubbleSortListener = new SortEventListener();
		bubbleSort.emitter.subscribe(bubbleSortListener);
		
		
		QuickSort quickSort = new QuickSort();
		SortEventListener quickSortListener = new SortEventListener();
		quickSort.emitter.subscribe(quickSortListener);
			
		int seed = 1;
		Random random = new Random(seed);
		int[] ints = new int[20];
		for (int i = 0; i < ints.length; i++) {
			ints[i] = random.nextInt(100);
		}
			
		System.out.println("Ints: ");
		for (int i = 0; i < ints.length; i++) {
			System.out.print(ints[i] + " ");
		}
		System.out.println("");
		
		System.out.println("Bubble Sort: ");
		int[] bubbleInput = Arrays.copyOf(ints, ints.length);
		bubbleSort.sort(bubbleInput);
		
		System.out.println("Ints: ");
		for (int i = 0; i < ints.length; i++) {
			System.out.print(ints[i] + " ");
		}
		System.out.println("");
			
		System.out.println("Quick Sort: ");
		int[] quickInput = Arrays.copyOf(ints, ints.length);
		quickSort.sort(quickInput);
	}
}

