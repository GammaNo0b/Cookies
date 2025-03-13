
package me.gamma.cookies.object;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import me.gamma.cookies.object.item.ItemProvider;
import me.gamma.cookies.util.collection.Pair;



/**
 * Helper class to provide useful methods for supplier.
 * 
 * @author gamma
 *
 */
public interface Supplier {

	/**
	 * Collects at most {@code max} resources of the given {@code type} from the {@code outputs}.
	 * 
	 * @param <T>     the type of the resource to be supplied
	 * @param <P>     the type of the provider
	 * @param type    the type to be supplied
	 * @param max     the maximum amount to be supplied
	 * @param outputs the list of {@link ItemProvider} to supply the resources
	 * @return the amount of resources that got collected from the supplier
	 */
	static <T, P extends Provider<T>> int supply(T type, int max, List<P> outputs) {
		int amount = 0;
		for(P provider : outputs) {
			if(provider.match(type)) {
				int extract = provider.get(max);
				amount += extract;
				max -= extract;
				if(max <= 0)
					return amount;
			}
		}
		return amount;
	}


	/**
	 * Collects at most {@code max} resources that either have no type or the type does not matter from the {@code outputs}.
	 * 
	 * @param <P>     the type of the provider which has to be a subclass of the {@link TypelessProvider}
	 * @param max     the maximum amount to be supplied
	 * @param outputs the list of {@link ItemProvider} to supply the resources
	 * @return the amount of resources that got collected from the supplier
	 */
	static <P extends TypelessProvider> int supplyTypeless(int max, List<P> outputs) {
		int amount = 0;
		for(TypelessProvider provider : outputs) {
			int extract = provider.get(max);
			amount += extract;
			max -= extract;
			if(max <= 0)
				return amount;
		}
		return amount;
	}


	/**
	 * Collects at most {@code max} resources of any type from the {@code outputs}.
	 * 
	 * @param <T>     the type of the resource
	 * @param <P>     the type of the provider
	 * @param max     the maximum amount of resources to be supplied
	 * @param outputs the list of {@link ItemProvider} to supply the resources
	 * @return the pair containing the type of resource and the amount that got collected from the supplier
	 */
	static <T, P extends Provider<T>> Pair<T, Integer> supply(int max, List<P> outputs) {
		return supply(_ -> max, outputs);
	}


	/**
	 * Collects resources of any type from the {@code outputs}. The {@code maxFunc} determines how many resources should be supplied at most depending on
	 * the type of resource.
	 * 
	 * @param <T>     the type of the resource
	 * @param <P>     the type of the provider
	 * @param maxFunc function to determine the maximum amount of resources to be supplied
	 * @param outputs the list of {@link ItemProvider} to supply the resources
	 * @return the pair containing the type of resource and the amount that got collected from the supplier
	 */
	static <T, P extends Provider<T>> Pair<T, Integer> supply(Function<T, Integer> maxFunc, List<P> outputs) {
		return supply(maxFunc, Filter.empty(), outputs);
	}


	/**
	 * Collects resources of any type from the {@code outputs} that pass the given {@code filter}. The {@code maxFunc} determines how many resources
	 * should be supplied at most depending on the type of resource.
	 * 
	 * @param <T>     the type of the resource
	 * @param <P>     the type of the provider
	 * @param <F>     the type of the filter
	 * @param maxFunc function to determine the maximum amount of resources to be supplied
	 * @param filter  the filter
	 * @param outputs the list of {@link ItemProvider} to supply the resources
	 * @return the pair containing the type of resource and the amount that got collected from the supplier
	 */
	static <T, P extends Provider<T>, F extends Filter<T>> Pair<T, Integer> supply(Function<T, Integer> maxFunc, F filter, List<P> outputs) {
		T type = null;
		int amount = 0;
		int max = 0;
		for(P provider : outputs) {
			if(type == null) {
				if(provider.isEmpty())
					continue;
				type = provider.getType();
				max = maxFunc.apply(type);
			} else if(!provider.match(type)) {
				continue;
			}
			int extract = provider.get(max, filter);
			amount += extract;
			max -= extract;
			if(max <= 0)
				return new Pair<>(type, amount);
		}
		return new Pair<>(type, amount);
	}


	/**
	 * Collects resources of any type from the {@code outputs} that pass the given {@code filter}. The {@code maxFunc} determines how many resources
	 * should be supplied at most depending on the type of resource. The {@code consumer} consumes the resulting items and returns what it could not
	 * consume.
	 * 
	 * @param <T>      the type of the resource to be supplied and consumed
	 * @param <P>      the type of the provider
	 * @param <F>      the type of the filter
	 * @param maxFunc  function to determine the maximum amount of resources to be supplied
	 * @param filter   the filter
	 * @param outputs  the list of {@link ItemProvider} to supply the resources
	 * @param consumer the consumer
	 * @return if any items got supplied and consumed
	 */
	static <T, P extends Provider<T>, F extends Filter<T>> boolean supply(Function<T, Integer> maxFunc, F filter, List<P> outputs, UnaryOperator<Pair<T, Integer>> consumer) {
		T type;
		int amount, max;

		outputs = new ArrayList<>(outputs);
		outputs.removeIf(Provider::isEmpty);

		Set<T> triedTypes = new HashSet<>();
		HashMap<P, Integer> suppliers = new HashMap<>();
		Iterator<P> iterator;
		boolean foundProvider = true;
		while(foundProvider) {
			foundProvider = false;
			type = null;
			amount = 0;
			max = 0;

			iterator = outputs.iterator();
			while(iterator.hasNext()) {
				P provider = iterator.next();
				if(triedTypes.contains(provider.getType())) {
					iterator.remove();
					continue;
				}

				if(type == null) {
					type = provider.getType();
					triedTypes.add(type);
					max = maxFunc.apply(type);
					foundProvider = true;
				} else if(!provider.match(type)) {
					continue;
				}
				int extract = provider.get(max, filter);
				if(extract <= 0)
					continue;

				suppliers.put(provider, extract);
				amount += extract;
				max -= extract;
				if(max <= 0)
					break;
			}

			if(type == null || amount == 0)
				return false;

			Pair<T, Integer> rest = consumer.apply(new Pair<>(type, amount));
			int i = rest.right;
			boolean transferred = i < amount;
			for(Map.Entry<P, Integer> entry : suppliers.entrySet()) {
				int insert = Math.min(entry.getValue(), i);
				entry.getKey().set(type, insert);
				i -= insert;
				if(i <= 0)
					break;
			}
			suppliers.clear();

			if(transferred)
				return true;
		}
		return false;
	}

}
