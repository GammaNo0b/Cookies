
package me.gamma.cookies.util;


public class Holder<T> implements Provider<T> {

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
	public void set(T value) {
		this.value = value;
	}

}
