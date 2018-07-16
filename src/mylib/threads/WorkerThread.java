package mylib.threads;

public class WorkerThread<IN, OUT> extends Thread {
	
	private final JobPool<IN, OUT> jobPool;
	
	public WorkerThread(JobPool<IN, OUT> jobPool) {
		this.jobPool = jobPool;
	}

	@Override
	public void run() {
		while (jobPool.hasJob()) {
			SingleThreadJob<IN, OUT> job = jobPool.getNextJobIfPresent();
			if (job == null) continue;
			OUT res = job.perform();
			jobPool.addResult(job.getId(), res);
		}
	}
	

}
