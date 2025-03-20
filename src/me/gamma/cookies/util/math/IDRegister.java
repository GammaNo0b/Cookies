
package me.gamma.cookies.util.math;


import java.util.HashMap;
import java.util.Map;



/**
 * Assigns to types of type T an int and stores them into a map.
 * 
 * @param <T> the type
 */
public class IDRegister<T> {

	private final IDGen<Integer> idgen = IDGen.newIntIDGen();
	private final Map<Integer, T> register = new HashMap<>();

	/**
	 * Registers the given value. Returns the assigned id.
	 * 
	 * @param value the value
	 * @return the id
	 */
	public int register(T value) {
		int id;
		do {
			id = this.idgen.generate();
		} while(this.register.containsKey(id));
		this.register.put(id, value);
		return id;
	}


	/**
	 * Unregisters the value with the given id.
	 * 
	 * @param id the id
	 * @return the removed value
	 */
	public T unregister(int id) {
		this.idgen.release(id);
		return this.register.remove(id);
	}


	/**
	 * Gets the value to the corresponding id.
	 * 
	 * @param id the id
	 * @return the value
	 */
	public T getValue(int id) {
		return this.register.get(id);
	}

}
