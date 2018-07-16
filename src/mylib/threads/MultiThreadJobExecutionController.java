package mylib.threads;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class MultiThreadJobExecutionController<IN,OUT> {

	private final Collection<WorkerThread<IN, OUT>> threads;
	
	public MultiThreadJobExecutionController() {
		this.threads = new ArrayList<>();
	}
	
	public Map<Long, OUT> execute(Collection<SingleThreadJob<IN, OUT>> _jobs) {
		JobPool<IN, OUT> jobs = new JobPool<>(_jobs);
		if (! jobs.hasJob()) {
			return null;
		}
		generateThreads(jobs);
		for (WorkerThread<IN, OUT> thread : threads) {
			thread.start();
		}
		while (jobs.hasJob()) {
			SingleThreadJob<IN, OUT> job = jobs.getNextJobIfPresent();
			if (job == null) continue;
			OUT res = job.perform();
			jobs.addResult(job.getId(), res);
		}
		
		for (WorkerThread<?,?> thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return jobs.getResults();
	}

	private void generateThreads(JobPool<IN,OUT> jobs) {
		for (int i = 0; i < Runtime.getRuntime().availableProcessors() -1 ; i++) {
			threads.add( new WorkerThread<>(jobs));
		}
	}
	
}
