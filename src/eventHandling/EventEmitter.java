package eventHandling;

public interface EventEmitter {
	public void emit(SortEvent event);
	
	public void subscribe(EventListener listener);
	
	public void unsubscribe(EventListener listener);
}
