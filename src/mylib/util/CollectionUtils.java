package mylib.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class CollectionUtils {
	
	public static <P,R> Collection<R> collectForEach(Collection<P> collection, Function<P,Collection<R>> delegate){
		List<R> res = new ArrayList<>();
		collection.forEach(p -> {
			res.addAll(delegate.apply(p));
		});
		return res;
	}

}
