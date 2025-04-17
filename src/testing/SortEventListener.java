package testing;

import eventHandling.EventListener;
import eventHandling.SortEvent;

public class SortEventListener implements EventListener{
	private long startTime;
	private long endTime;
	public long executionTime;
	public int comparisons;
	public int[] currentArray;

	public void onEvent(SortEvent event) {
		//This class was just for testing.
		switch(event.getType()) {
		case TIMER_START:
			startTime = System.nanoTime();
			break;
			
		case TIMER_STOP:
			endTime = System.nanoTime();
			executionTime = endTime - startTime;
			break;
			
		case INCREMENT_COMPARISONS:
			comparisons++;
			break;
			
		case INITIALIZE_ARRAY:
			currentArray = event.getArray();
			for (int i = 0; i < currentArray.length; i++) {
				System.out.print(currentArray[i] + " ");
			}
			
			System.out.println("");
			break;
			
		case UPDATE_ARRAY:
			for (int i = event.getHead(); i < event.getTail(); i++) {
				currentArray[i] = event.getArray()[i];
			}
			
			for (int i = 0; i < currentArray.length; i++) {
				System.out.print(currentArray[i] + " ");
			}
			
			System.out.println("");
			break;
		}
	}
}