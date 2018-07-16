package mylib.threads.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import mylib.threads.MultiThreadJobExecutionController;
import mylib.threads.SingleThreadJob;

public class TestExecutionController {
	
	class CountDivbyThreeJob extends SingleThreadJob<Integer, Integer> {

		public CountDivbyThreeJob(Integer input, long id, int priority) {
			super(input, id, priority);
		}

		@Override
		public Integer perform() {
			int res = 0;
			for (int i = 1; i <= input; i++) {
				System.out.println("Current Job: "+getId());
				if (i % 3 == 0) {
					res++;
				}
			}
			return res;
		}
		
	}
	
	@Test
	public void test() {
		Collection<SingleThreadJob<Integer, Integer>> jobs = new ArrayList<>();
		for (int i = 0; i <= 10000; i+=50 ) {
			jobs.add(new CountDivbyThreeJob(Integer.valueOf(i), (long)i, 1));
		}
		Map<Long, Integer> res =  new MultiThreadJobExecutionController<Integer, Integer>().execute(jobs);
		for (int i = 5; i <= 50; i+=5 ) {
			System.out.println("Up to "+i +" there are " + res.get(Long.valueOf(i)) + " number div by 3");
		}
	}
	

}
