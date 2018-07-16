package mylib.util.test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import mylib.util.CollectionUtils;
import mylib.util.HasId;

public class UtilTest {
	
	class ValueId implements HasId {
		
		public ValueId(int n) {
			id = n;
		}
		
		public int id;

		@Override
		public int getId() {
			return id;
		}
	}
	
	@Test
	public void testPartition() {
		Collection<Collection<Integer>>  res = CollectionUtils.getRandomIdPartition(makeIds(1,2,9,10, 0), 2);
		res = CollectionUtils.getRandomIdPartition(makeIds(), 2);
		res = CollectionUtils.getRandomIdPartition(makeIds(0,1,2,3), 2);
		res = CollectionUtils.getRandomIdPartition(makeIds(0,1,2,3), 3);
		System.out.println();
	}
	
	
	public List<ValueId> makeIds(Integer... args) {
		return Arrays.asList(args).stream().map(i -> new ValueId(i)).collect(Collectors.toList());
	}

	

}
