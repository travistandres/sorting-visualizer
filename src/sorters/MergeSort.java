package sorters;

import eventHandling.EventType;
import eventHandling.SortEvent;
import eventHandling.SortEventEmitter;

public class MergeSort {
    public SortEventEmitter emitter = new SortEventEmitter();

    public int[] sort(int[] ints) {
        emitter.emit(new SortEvent(EventType.TIMER_START));
        emitter.emit(new SortEvent(EventType.INITIALIZE_ARRAY, ints));

        mergeSort(ints, 0, ints.length - 1);

        emitter.emit(new SortEvent(EventType.TIMER_STOP));
        emitter.emit(new SortEvent(EventType.SORT_COMPLETE, ints, -1, -1));
        return ints;
    }

    private void mergeSort(int[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);

            merge(array, left, mid, right);
        }
    }

    private void merge(int[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
    
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];
    
        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, mid + 1, rightArray, 0, n2);
    
        int i = 0, j = 0, k = left;
    
        while (i < n1 && j < n2) {
            emitter.emit(new SortEvent(EventType.INCREMENT_COMPARISONS));
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            emitter.emit(new SortEvent(EventType.UPDATE_ARRAY, array, left, right)); // Emit with range
            k++;
        }
    
        while (i < n1) {
            array[k] = leftArray[i];
            emitter.emit(new SortEvent(EventType.UPDATE_ARRAY, array, left, right)); // Emit with range
            i++;
            k++;
        }
    
        while (j < n2) {
            array[k] = rightArray[j];
            emitter.emit(new SortEvent(EventType.UPDATE_ARRAY, array, left, right)); // Emit with range
            j++;
            k++;
        }
    }private void merge(int[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
    
        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];
    
        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, mid + 1, rightArray, 0, n2);
    
        int i = 0, j = 0, k = left;
    
        while (i < n1 && j < n2) {
            emitter.emit(new SortEvent(EventType.INCREMENT_COMPARISONS));
            if (leftArray[i] <= rightArray[j]) {
                array[k] = leftArray[i];
                i++;
            } else {
                array[k] = rightArray[j];
                j++;
            }
            emitter.emit(new SortEvent(EventType.UPDATE_ARRAY, array, left, right)); // Emit with range
            k++;
        }
    
        while (i < n1) {
            array[k] = leftArray[i];
            emitter.emit(new SortEvent(EventType.UPDATE_ARRAY, array, left, right)); // Emit with range
            i++;
            k++;
        }
    
        while (j < n2) {
            array[k] = rightArray[j];
            emitter.emit(new SortEvent(EventType.UPDATE_ARRAY, array, left, right)); // Emit with range
            j++;
            k++;
        }
    }
}