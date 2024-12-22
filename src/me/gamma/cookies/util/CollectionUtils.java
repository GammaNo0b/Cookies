
package me.gamma.cookies.util;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;



public class CollectionUtils {

	/**
	 * {@link Random} instance to generate random numbers.
	 */
	private static final Random r = new Random();

	/**
	 * Returns a random element from the list or null if the list is empty.
	 * 
	 * @param <T>  the type of elements stored
	 * @param list the list
	 * @return the random element
	 */
	public static <T> T randomElement(List<T> list) {
		return list.isEmpty() ? null : list.get(r.nextInt(list.size()));
	}


	/**
	 * Returns a sublist from the given list that starts at the given starting position hand has the given predetermined maximum length.
	 * 
	 * @param <T>   the type of elements stored
	 * @param list  the list
	 * @param start the starting position
	 * @param limit maximum length of the sublist
	 * @return the sublist
	 */
	public static <T> List<T> subList(List<T> list, int start, int limit) {
		return list.subList(start, Math.min(list.size(), start + limit));
	}


	/**
	 * Returns a new list with each value in the original list mapped.
	 * 
	 * @param <T>    the original type
	 * @param <R>    the mapped type
	 * @param list   the original list
	 * @param mapper the mapping function
	 * @return the mapped list
	 */
	public static <T, R> List<R> mapList(List<T> list, Function<T, R> mapper) {
		return list.stream().map(mapper).toList();
	}


	/**
	 * Converts the given list to a map.
	 * 
	 * @param <T>            the type stored in the list
	 * @param <K>            the type of the key stored in the map
	 * @param <V>            the type of the value stored in the map
	 * @param <M>            the type of the map
	 * @param list           the list
	 * @param mapGenerator   the generator function to get a new map
	 * @param keyGenerator   the generator function to get the key from an element from the list
	 * @param valueGenerator the generator function to get the value from an element from the list
	 * @return the converted map
	 */
	public static <T, K, V, M extends Map<K, V>> M listToMap(List<T> list, Supplier<M> mapGenerator, Function<T, K> keyGenerator, Function<T, V> valueGenerator) {
		M map = mapGenerator.get();
		for(T t : list)
			map.put(keyGenerator.apply(t), valueGenerator.apply(t));
		return map;
	}


	/**
	 * Merges all values in the given map with the given mapper function and returns a stream containing the merged values.
	 * 
	 * @param <K>    the key type of the map
	 * @param <V>    the value type of the map
	 * @param <T>    the type of the new merged value
	 * @param map    the map
	 * @param mapper the mapper function
	 * @return a stream containing the merged values from the map
	 */
	public static <K, V, T> Stream<T> merge(Map<K, V> map, BiFunction<K, V, T> mapper) {
		return map.entrySet().stream().map(e -> mapper.apply(e.getKey(), e.getValue()));
	}


	/**
	 * Counts the element in the given collection. The element will be stored as key in the map whereas the value represents the number of times it
	 * occurred in the collection.
	 * 
	 * @param <T>        the type of element
	 * @param collection the collection
	 * @param generator  the generator to supply the map
	 * @return the counted elements stored in the generated map
	 */
	public static <T> Map<T, Integer> count(Collection<? extends T> collection, Supplier<Map<T, Integer>> generator) {
		Map<T, Integer> map = generator.get();
		for(T t : collection)
			if(t != null)
				map.put(t, 1 + map.getOrDefault(t, 0));
		return map;
	}


	/**
	 * Counts the element in the given stream. The element will be stored as key in the map whereas the value represents the number of times it occurred
	 * in the stream.
	 * 
	 * @param <T>       the type of element
	 * @param stream    the stream
	 * @param generator the generator to supply the map
	 * @return the counted elements stored in the generated map
	 */
	public static <T> Map<T, Integer> count(Stream<? extends T> stream, Supplier<Map<T, Integer>> generator) {
		Map<T, Integer> map = generator.get();
		stream.forEach(t -> {
			if(t != null)
				map.put(t, 1 + map.getOrDefault(t, 0));
		});
		return map;
	}

}
