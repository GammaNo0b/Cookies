
package me.gamma.cookies.util.math;


import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.google.common.base.Supplier;



/**
 * This ID Generator generates random integers that can be used for unique identification.
 * 
 * @author gamma
 *
 */
public interface IDGen<T> {

	/**
	 * The random generator to generate new ids.
	 */
	Random random = new Random();

	/**
	 * Registers the id so that on calling {@link IDGen#exists(T)} true is returned and ids are not generated twice.
	 * 
	 * @param id the id
	 */
	void register(T id);

	/**
	 * Unregisters the id so that on calling {@link IDGen#exists(T)} false is returned and the id can be generated again.
	 * 
	 * @param id the id
	 */
	void release(T id);

	/**
	 * Unregisters all registered ids.
	 */
	void reset();

	/**
	 * Checks if the given id already exists.
	 * 
	 * @param id the id
	 * @return fi the id already exists
	 */
	boolean exists(T id);

	/**
	 * Generated a new random id.
	 * 
	 * @return a new random id.
	 */
	T randomId();


	/**
	 * Generates a new id and returns it.
	 * 
	 * @return the new id
	 */
	default T generate() {
		T id;
		do {
			id = this.randomId();
		} while(this.exists(id));
		this.register(id);
		return id;
	}

	public static interface SetIDGen<T> extends IDGen<T> {

		/**
		 * Returns the set of all registered ids.
		 * 
		 * @return the registered ids
		 */
		Set<T> registeredIds();


		@Override
		default void register(T id) {
			this.registeredIds().add(id);
		}


		@Override
		default void release(T id) {
			this.registeredIds().remove(id);
		}


		@Override
		default void reset() {
			this.registeredIds().clear();
		}


		@Override
		default boolean exists(T id) {
			return this.registeredIds().contains(id);
		}

	}

	public static <T> SetIDGen<T> newIDGen(Supplier<T> generator) {
		return new SetIDGen<T>() {

			private final HashSet<T> ids = new HashSet<>();

			@Override
			public Set<T> registeredIds() {
				return this.ids;
			}


			@Override
			public T randomId() {
				return generator.get();
			}

		};
	}


	public static SetIDGen<Integer> newIntIDGen() {
		return newIDGen(random::nextInt);
	}

}
