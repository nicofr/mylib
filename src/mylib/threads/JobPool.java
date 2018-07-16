package mylib.threads;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;

public final class JobPool<IN, OUT> {
	
	private final PriorityBlockingQueue<SingleThreadJob<IN, OUT>> jobs;
	private final Map<Long, OUT> results;
	
	public JobPool(Collection<SingleThreadJob<IN, OUT>> jobs) {
		this.jobs = new PriorityBlockingQueue<>(jobs);
		results = new HashMap<>();
	}
	
	public final synchronized void addResult(long id, OUT result) {
		this.results.put(id, result);
	}
	
	public final synchronized OUT getResult(long id) {
		return results.get(id);
	}
	
	public final boolean hasJob() {
		return !jobs.isEmpty();
	}
	
	public final synchronized SingleThreadJob<IN,  OUT> getNextJobIfPresent() {
		return jobs.poll();
	}
	
	public final synchronized void addJob(SingleThreadJob< IN, OUT> job) {
		this.jobs.add(job);
	}

	public Map<Long, OUT> getResults() {
		return results;
	}

}
