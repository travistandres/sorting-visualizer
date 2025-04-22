package sorters;
import java.util.ArrayList;

import eventHandling.EventType;
import eventHandling.SortEvent;
import eventHandling.SortEventEmitter;

public class QuickSort {
	public SortEventEmitter emitter = new SortEventEmitter();
	
	public int[] sort(int[] ints) {
		emitter.emit(new SortEvent(EventType.TIMER_START));
		emitter.emit(new SortEvent(EventType.INITIALIZE_ARRAY, ints));
		
		//Quick sort.
		int[] scratch = new int[ints.length];
		ints = partition(ints, 0, ints.length, scratch);
		
		emitter.emit(new SortEvent(EventType.TIMER_STOP));
		emitter.emit(new SortEvent(EventType.SORT_COMPLETE, ints, -1, -1));
		return ints;
	}
	
	private int[] partition(int[] ints, int head, int tail, int[] scratch) {
		if (tail - head <= 1) {
			return ints;
		}
		
		else {
			int pivot = ints[head + (tail - head) / 2];
			ArrayList<Integer> lower = new ArrayList<>();
			ArrayList<Integer> equal = new ArrayList<>();
			ArrayList<Integer> higher = new ArrayList<>();
			
			for (int i = head; i < tail; i++) {
				if (ints[i] < pivot) {
					lower.add(ints[i]);
				}
				
				else if (ints[i] > pivot) {
					higher.add(ints[i]);
				}
				
				else {
					equal.add(ints[i]);
				}
				
				emitter.emit(new SortEvent(EventType.INCREMENT_COMPARISONS));
			}
			
			int scratchIndex = 0;
			for (int i : lower) {
				scratch[scratchIndex++] = i;
			}
			
			for (int i : equal) {
				scratch[scratchIndex++] = i;
			}
			
			for (int i : higher) {
				scratch[scratchIndex++] = i;
			}
			
			for (int i = 0; i < scratchIndex; i++) {
				ints[head + i] = scratch[i];
			}
			
			emitter.emit(new SortEvent(EventType.UPDATE_ARRAY, ints.clone(), head, tail));

	        partition(ints, head, head + lower.size(), scratch);
	        partition(ints, head + lower.size() + equal.size(), tail, scratch);
			
			return ints;
		}
	}
}