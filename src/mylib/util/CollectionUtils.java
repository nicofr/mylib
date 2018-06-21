package mylib.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CollectionUtils {
	
	/**
	 * collector function for functions with collection return value
	 * @param collection
	 * @param delegate
	 * @return
	 */
	public static <P,R> Collection<R> collectForEach(Collection<P> collection, Function<P,Collection<R>> delegate){
		List<R> res = new ArrayList<>();
		collection.forEach(p -> {
			res.addAll(delegate.apply(p));
		});
		return res;
	}

	
	/**
	 * casts collection to given type
	 * @param arg
	 * @return
	 */
	public static <TYPE> Collection<TYPE> cast(Collection<? extends TYPE> arg) {
		Collection<TYPE> res = new ArrayList<>();
		res.addAll(arg);
		return res;
	}
	
	
	/**
	 * generates a list with a single entry
	 * @param arg
	 * @return
	 */
	public static <TYPE> List<TYPE> single(TYPE arg) {
		List<TYPE> res = new ArrayList<>();
		res.add(arg);
		return res;
	}


	/**
	 * generates empty collection 
	 * @return
	 */
	public static <T> Collection<T> empty() {
		return new ArrayList<>();
	}

	/**
	 * checks given collections are equal, ie both hare subset to each other
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public static <T> boolean setEquals(Collection<T> arg0, Collection<T> arg1) {
		return arg0.size() == arg1.size() && arg0.stream().allMatch(a -> arg1.contains(a));
	}
	
	/**
	 * 
	 */
	public static <T> Collection<T> filter(Collection<T> c, Predicate<T> p) {
		return c.stream().filter(p).collect(Collectors.toList());
	}
	
	/**
	 * calls toString() on all elements of given colleciton
	 * @param arg
	 * @return
	 */
	public static Collection<String> toString(Collection<?> arg) {
		return arg.stream().map(o -> o.toString()).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @param arg
	 * @return
	 */
	public static Collection<Integer> toIds(Collection<? extends HasId> arg) {
		return arg.stream().map(h -> h.getId()).collect(Collectors.toList());
	}

}
