package sorters;
import eventHandling.EventType;
import eventHandling.SortEvent;
import eventHandling.SortEventEmitter;

public class BubbleSort {
	public SortEventEmitter emitter = new SortEventEmitter();

	public int[] sort(int[] ints) {
		emitter.emit(new SortEvent(EventType.TIMER_START));
		emitter.emit(new SortEvent(EventType.INITIALIZE_ARRAY, ints));
		
		//Bubble sort.
		for (int i = 0; i < ints.length; i++) {
			boolean swapped = false;
			for (int j = 0; j < (ints.length - 1); j++) {
				if (ints[j] > ints[j + 1]) {
					int temp = ints[j];
					ints[j] = ints[j + 1];
					ints[j + 1] = temp;
					
					emitter.emit(new SortEvent(EventType.UPDATE_ARRAY, ints, j, j + 1));
					
					swapped = true;
				}
				emitter.emit(new SortEvent(EventType.INCREMENT_COMPARISONS));
			}
			
			if (!swapped) {
				break;
			}
		}
		
		emitter.emit(new SortEvent(EventType.TIMER_STOP));
		return ints;
	}
}
