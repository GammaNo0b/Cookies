
package me.gamma.cookies.util.collection;


import java.util.Map;
import java.util.function.Supplier;



public class MapUtils {

	/**
	 * Returns the value under the given key or stores def under the key and returns it. In each case there will be something stored under key after this
	 * operation.
	 * 
	 * @param <K> the key type of the map
	 * @param <V> the value type of the map
	 * @param map the map
	 * @param key the key
	 * @param def the default value
	 * @return the value in the map under key
	 */
	public static <K, V> V getOrStoreDefault(Map<K, V> map, K key, V def) {
		if(!map.containsKey(key))
			map.put(key, def);

		return map.get(key);
	}

	/**
	 * Returns the value under the given key or stores def under the key and returns it. In each case there will be something stored under key after this
	 * operation.
	 * 
	 * @param <K> the key type of the map
	 * @param <V> the value type of the map
	 * @param map the map
	 * @param key the key
	 * @param def the default value
	 * @return the value in the map under key
	 */
	public static <K, V> V getOrStoreDefault(Map<K, V> map, K key, Supplier<V> def) {
		if(!map.containsKey(key))
			map.put(key, def.get());

		return map.get(key);
	}

}
