package mylib.threads;

public abstract class SingleThreadJob<IN,OUT> implements Comparable<SingleThreadJob<IN, OUT>> {
	
	protected final IN input;
	private final long id;
	private final int priority;
	
	public SingleThreadJob(final IN input, final long id, final int priority) {
		this.input = input;
		this.id = id;
		this.priority = priority;
	}
	
	public abstract OUT perform();

	public final long getId() {
		return id;
	}
	
	public final int getPriority() {
		return priority;
	}

	@Override
	public int compareTo(SingleThreadJob<IN, OUT> arg0) {
		return priority > arg0.getPriority() ? 1 : priority < arg0.getPriority() ? -1 : 0;
	}
	
}
