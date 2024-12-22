
package me.gamma.cookies.object;


import java.util.List;

import me.gamma.cookies.object.item.ItemProvider;



/**
 * Helper class to provide useful methods for consumer.
 * 
 * @author gamma
 *
 */
public interface Consumer {

	/**
	 * Consumes {@code amount} amount of the resource of the given {@code type} with the given list of inputs.
	 * 
	 * @param <T>    The type of the resource to be consumed by the consumers
	 * @param <P>    The type of the provider
	 * @param type   the type of the resource to be consumed
	 * @param amount the maximum amount to be consumed
	 * @param inputs the list of {@link ItemProvider} to consume the resource
	 * @return the amount that could not be consumed
	 */
	static <T, P extends Provider<T>> int consume(T type, int amount, List<P> inputs) {
		// store in non-empty provider
		for(P provider : inputs) {
			if(provider.isEmpty())
				continue;

			if(provider.match(type))
				if((amount = provider.set(amount)) == 0)
					return 0;
		}

		// store in empty provider
		for(P provider : inputs) {
			if(!provider.isEmpty())
				continue;

			if(!provider.match(type)) {
				if(!provider.canChangeType(type))
					continue;

				provider.setType(type);
			}

			if((amount = provider.set(type, amount)) == 0)
				return 0;
		}

		return amount;
	}


	/**
	 * Consumes {@code amount} of a resource that either has no type or the type does not matter with the given list of inputs.
	 * 
	 * @param <P>    the type of the provider which has to be a subclass of the {@link TypelessProvider}
	 * @param amount the amount of the resource to be consumed
	 * @param inputs the list of {@link ItemProvider} to consume the resource
	 * @return the amount that could not be consumed
	 */
	static <P extends TypelessProvider> int consumeTypeless(int amount, List<P> inputs) {
		for(TypelessProvider provider : inputs)
			if((amount = provider.set(amount)) == 0)
				return 0;
		return amount;
	}

}
