package eventHandling;
import java.util.ArrayList;

public class SortEventEmitter implements EventEmitter {
	ArrayList<EventListener> listeners = new ArrayList<EventListener>();
	
	public void emit(SortEvent event) {
		for (EventListener s : listeners) {
			s.onEvent(event);
		}
	}
	
	public void subscribe(EventListener listener) {
		listeners.add(listener);
	}

	public void unsubscribe(EventListener listener) {
		listeners.remove(listener);
	}
}
