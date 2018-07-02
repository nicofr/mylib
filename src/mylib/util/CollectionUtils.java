package mylib.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import mylib.collections.Pair;
import mylib.collections.TwoKeyMap;

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
	 * Generates list of csv values for map
	 * @param map
	 * @return
	 */
	public static List<String> toCSV(Map<?,?> map) {
		return map.entrySet().stream().map(e -> e.getKey().toString() + ","+e.getValue().toString()).collect(Collectors.toList());
	}
	
	/**
	 * Generates a collection of csv entries
	 * @param arg
	 * @return
	 */
	public static Collection<String> toCSV(Collection<? extends CSVable> arg) {
		return map(arg, o -> o.toCSV());
	}

	
	/**
	 * calculates inverse mapping of given argument
	 * @param map
	 * @param arg
	 * @return
	 */
	public static <L,R> Collection<L> mapInverse(Map<L,R> map, R arg) {
		return map.keySet().stream().filter(key -> map.get(key).equals(arg)).collect(Collectors.toList());
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
	 * generates union of lists
	 * @param lists
	 * @return
	 */
	@SafeVarargs
	public static <T> List<T> union(List<T>... lists) {
		List<T> res = new ArrayList<>();
		for (int i = 0; i < lists.length; i++) {
			res.addAll(lists[i]);
		}
		return res;
	}
	
	
	/**
	 * generates a list with a single entry
	 * @param arg
	 * @return
	 */
	public static <TYPE> List<TYPE> single(TYPE arg) {
		ArrayList<TYPE> res = new ArrayList<>();
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
	 * for easier and prettier code
	 */
	public static <T> Collection<T> filter(Collection<T> c, Predicate<T> p) {
		return c.stream().filter(p).collect(Collectors.toList());
	}
	
	/**
	 * calls toString() on all elements of given collection
	 * @param arg
	 * @return
	 */
	public static Collection<String> toString(Collection<?> arg) {
		return map(arg, o -> o.toString());
	}
	
	/**
	 * 
	 * @param arg
	 * @return
	 */
	public static Collection<Integer> toIds(Collection<? extends HasId> arg) {
		return map(arg, o -> o.getId());
	}
	
	/**
	 * 
	 * @param collection
	 * @param f
	 * @return
	 */
	public static <T,R> Collection<R> map(Collection<T> collection, Function<T,R> f) {
		return collection.stream().map(o -> f.apply(o)).collect(Collectors.toList());
	}
	
	/**
	 * Generates a list from given parameters
	 * @param args
	 * @return
	 */
	@SafeVarargs
	public static <T> List<T> toList(T... args) {
		return Arrays.asList(args);
	}
	
	/**
	 * Generates a submap of the given two key map
	 * @param map
	 * @param key2
	 * @return
	 */
	public static <K1, K2, V> Map<K1, V> subMapFirstKey(TwoKeyMap<K1, K2, V> map, K2 key2) {
		Map<K1, V> res = new HashMap<>();
		Collection<Entry<Pair<K1, K2>, V>> submap = map.entrySet().stream().filter(e -> e.getKey().getR().equals(key2)).collect(Collectors.toList());
		submap.forEach(e -> res.put(e.getKey().getL(), e.getValue()));
		return res;
	}
	
	/**
	 * Generates a submap of the given two key map
	 * @param map
	 * @param key2
	 * @return
	 */
	public static <K1, K2, V> Map<K2, V> subMapSecondKey(TwoKeyMap<K1, K2, V> map, K1 key1) {
		Map<K2, V> res = new HashMap<>();
		Collection<Entry<Pair<K1, K2>, V>> submap = map.entrySet().stream().filter(e -> e.getKey().getL().equals(key1)).collect(Collectors.toList());
		submap.forEach(e -> res.put(e.getKey().getR(), e.getValue()));
		return res;
	}
	
	/**
	 * 
	 * @param csvcontent
	 * @return
	 */
	public static Map<String, String> readCSVMap(List<String> csvcontent, String splt) {
		Map<String, String> res = new HashMap<>();
		for (String s : csvcontent) {
			res.put(s.split(splt)[0], s.split(splt)[1]);
		}
		return res;
	}
	
	/**
	 * 
	 * @param csvcontent
	 * @return
	 */
	public static Map<Integer, Integer> readCSVIntegerMap(List<String> csvcontent, String splt) {
		Map<Integer, Integer> res = new HashMap<>();
		for (String s : csvcontent) {
			res.put(Integer.valueOf(s.split(splt)[0]), Integer.valueOf(s.split(splt)[1]));
		}
		return res;
	}

}
