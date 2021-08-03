
package me.gamma.cookies.util;


import java.util.function.Consumer;
import java.util.function.Supplier;



public class Holder<T> implements Supplier<T>, Consumer<T> {

	private T value;


	public Holder() {
		this.value = null;
	}


	public Holder(T value) {
		this.value = value;
	}


	@Override
	public T get() {
		return value;
	}


	@Override
	public void accept(T value) {
		this.value = value;
	}

}
