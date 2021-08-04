
package me.gamma.cookies.util;


import java.util.function.Consumer;
import java.util.function.Supplier;



public interface Provider<T> extends Supplier<T>, Consumer<T> {

	void set(T t);


	@Override
	default void accept(T t) {
		this.set(t);
	}

}
