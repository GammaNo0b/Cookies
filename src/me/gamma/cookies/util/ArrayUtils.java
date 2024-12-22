
package me.gamma.cookies.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;



public class ArrayUtils {

	/**
	 * Converts the given elements to an array and returns it.
	 * 
	 * @param <T>      the element type
	 * @param elements the elements
	 * @return the array
	 */
	@SafeVarargs
	public static <T> T[] array(T... elements) {
		return elements;
	}


	/**
	 * Creates and returns an array containing all elements fron the given collection.
	 * 
	 * @param <T>        the element type
	 * @param collection the collection
	 * @param generator  the array generating function
	 * @return the created array
	 */
	public static <T> T[] array(Collection<T> collection, IntFunction<T[]> generator) {
		return collection.toArray(generator.apply(collection.size()));
	}


	/**
	 * Generates a new array with the given length. Every element will be created using the elementGenerato function, that will take the index at which
	 * the item will be stored and yield the element.
	 * 
	 * @param <T>              the type of the elements
	 * @param length           the length of the array
	 * @param elementGenerator the generator to generate the elements
	 * @param arrayGenerator   the array generator
	 * @return
	 */
	public static <T> T[] generate(int length, IntFunction<T> elementGenerator, IntFunction<T[]> arrayGenerator) {
		T[] array = arrayGenerator.apply(length);
		for(int i = 0; i < length; i++)
			array[i] = elementGenerator.apply(i);
		return array;
	}


	/**
	 * Returns an array containing the given arrays pasted behind each other.
	 * 
	 * @param <T>       the type of the elements
	 * @param generator the array generator
	 * @param arrays    the arrays
	 * @return the combined array
	 */
	@SafeVarargs
	public static <T> T[] combine(IntFunction<T[]> generator, T[]... arrays) {
		int len = 0;
		for(T[] array : arrays)
			len += array.length;

		T[] t = generator.apply(len);
		int i = 0;
		for(T[] array : arrays) {
			int l = array.length;
			System.arraycopy(array, 0, t, i, l);
			i += l;
		}

		return t;
	}


	/**
	 * Checks if the array contains the given element.
	 * 
	 * @param array the array to be checked
	 * @param value the searched value
	 * @return if the element is contained in the array
	 */
	public static <T> boolean contains(int[] array, int value) {
		for(int element : array)
			if(value == element)
				return true;
		return false;
	}


	/**
	 * Checks if the array contains the given element.
	 * 
	 * @param array the array to be checked
	 * @param value the searched value
	 * @return if the element is contained in the array
	 */
	public static <T> boolean contains(T[] array, T value) {
		for(T element : array)
			if(Objects.equals(element, value))
				return true;
		return false;
	}


	/**
	 * Filters the original array with the given filter and creates a new array containing only the filtered elements.
	 * 
	 * @param original  the original array
	 * @param filter    the filter
	 * @param generator the generator to create a new array of the generic type with the given length
	 * @return the new filtered array
	 */
	public static <T> T[] filter(T[] original, Predicate<T> filter, IntFunction<T[]> generator) {
		return Arrays.stream(original).filter(filter).toArray(generator);
	}


	/**
	 * Maps all elements from the given array and returns them in a new array.
	 * 
	 * @param <T>       the old type
	 * @param <R>       the new type
	 * @param original  the original array
	 * @param mapper    the mapping function
	 * @param generator the function to generate a new array
	 * @return the mapped array
	 */
	public static <T, R> R[] map(T[] original, Function<T, R> mapper, IntFunction<R[]> generator) {
		R[] array = generator.apply(original.length);
		for(int i = 0; i < original.length; i++)
			array[i] = mapper.apply(original[i]);
		return array;
	}


	/**
	 * Maps all elements from the given array and returns them in a list.
	 * 
	 * @param <T>      the old type
	 * @param <R>      the new type
	 * @param original the original array
	 * @param mapper   the mapping function
	 * @return the mapped array
	 */
	public static <T, R> ArrayList<R> mapToList(T[] original, Function<T, R> mapper) {
		ArrayList<R> list = new ArrayList<>();
		for(T t : original)
			list.add(mapper.apply(t));
		return list;
	}


	/**
	 * Creates a string filled with one character.
	 * 
	 * @param c      the character the string should be filled with
	 * @param length the length of the string
	 * @return the created string
	 */
	public static String fill(char c, int length) {
		char[] chars = new char[length];
		Arrays.fill(chars, c);
		return new String(chars);
	}


	/**
	 * Creates an array list and fills it with the given elements.
	 * 
	 * @param <E>      the generic type
	 * @param elements the elements
	 * @return the created array list
	 */
	@SafeVarargs
	public static <E> ArrayList<E> asList(E... elements) {
		ArrayList<E> list = new ArrayList<>();
		for(E e : elements)
			list.add(e);
		return list;
	}


	/**
	 * Returns the inverse array to the given array. The inverse array at the i-th position stores the index of the original array, in which the number i
	 * is stored. If the number is is not stored in the original array, it stores the given default value instead. The inverse array works like an inverse
	 * function in mathematics.
	 * 
	 * @param array the original array
	 * @return the default value
	 */
	public static int[] inverse(int[] array, int def) {
		int len = array.length;

		if(len == 0)
			return new int[0];

		int max = array[0];
		for(int i = 1; i < len; i++)
			if(max < array[i])
				max = array[i];

		int[] inverse = new int[max + 1];

		Arrays.fill(inverse, def);
		for(int i = 0; i < len; i++)
			inverse[array[i]] = i;

		return inverse;
	}

}
