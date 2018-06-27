package mylib.collections;

import java.util.HashMap;

@SuppressWarnings("serial")
public class TwoKeyMap<K1, K2, V> extends HashMap<Pair<K1, K2>,V> {

	public void put(K1 key1, K2 key2, V value) {
		 put(new Pair<K1, K2>(key1, key2), value);
	}
	
	public V get(K1 key1, K2 key2) {
		return get(new Pair<K1, K2>(key1, key2));
	}
	
	
}
