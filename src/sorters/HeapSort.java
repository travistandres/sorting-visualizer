package sorters;

import eventHandling.EventType;
import eventHandling.SortEvent;
import eventHandling.SortEventEmitter;

public class HeapSort {
    public SortEventEmitter emitter = new SortEventEmitter();

    public int[] sort(int[] ints) {
        emitter.emit(new SortEvent(EventType.TIMER_START));
        emitter.emit(new SortEvent(EventType.INITIALIZE_ARRAY, ints));

        int n = ints.length;

        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(ints, n, i);
        }

        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            int temp = ints[0];
            ints[0] = ints[i];
            ints[i] = temp;

            emitter.emit(new SortEvent(EventType.UPDATE_ARRAY, ints, 0, i));

            // Call heapify on the reduced heap
            heapify(ints, i, 0);
        }

        emitter.emit(new SortEvent(EventType.TIMER_STOP));
        emitter.emit(new SortEvent(EventType.SORT_COMPLETE, ints, -1, -1));
        return ints;
    }

    private void heapify(int[] ints, int n, int i) {
        int largest = i; // Initialize largest as root
        int left = 2 * i + 1; // Left child
        int right = 2 * i + 2; // Right child

        // If left child is larger than root
        if (left < n && ints[left] > ints[largest]) {
            largest = left;
        }

        // If right child is larger than largest so far
        if (right < n && ints[right] > ints[largest]) {
            largest = right;
        }

        // If largest is not root
        if (largest != i) {
            int swap = ints[i];
            ints[i] = ints[largest];
            ints[largest] = swap;

            emitter.emit(new SortEvent(EventType.UPDATE_ARRAY, ints, i, largest));

            // Recursively heapify the affected sub-tree
            heapify(ints, n, largest);
        }

        emitter.emit(new SortEvent(EventType.INCREMENT_COMPARISONS));
    }
}