package eventHandling;
import java.util.Optional;

public class SortEvent {
	private final EventType type;
	private final Optional<int[]> ints;
	private final Optional<Integer> head;
	private final Optional<Integer> tail;
	
	public SortEvent(EventType type) {
		this.type = type;
		this.ints = Optional.empty();
		this.head = Optional.empty();
		this.tail = Optional.empty();
	}
	
	//Used for INITIALIZE_ARRAY.
	public SortEvent(EventType type, int[] ints) {
		this.type  = type;
		this.ints = Optional.of(ints);
		this.head = Optional.empty();
		this.tail = Optional.empty();
	}
	
	//Used for UPDATE_ARRAY.
	public SortEvent(EventType type, int[] ints, int head, int tail) {
		this.type  = type;
		this.ints = Optional.of(ints);
		this.head = Optional.of(head);
		this.tail = Optional.of(tail);
	}
	
	public EventType getType() {
		return type;
	}
	
	public int[] getArray() {
		if (ints.isPresent()) {
			return ints.get();
		}
		
		else {
			throw new NullPointerException("This event type holds no array.");
		}
	}
	
	public int getHead() {
		if (ints.isPresent()) {
			return head.get();
		}
		
		else {
			throw new NullPointerException("No value present.");
		}
	}
	
	public int getTail() {
		if (ints.isPresent()) {
			return tail.get();
		}
		
		else {
			throw new NullPointerException("No value present.");
		}
	}
}
