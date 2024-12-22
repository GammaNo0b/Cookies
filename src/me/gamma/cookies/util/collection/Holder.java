
package me.gamma.cookies.util.collection;


public class Holder<T> {

	public T value;

	public Holder() {
		this.value = null;
	}


	public Holder(T value) {
		this.value = value;
	}


	public T get() {
		return this.value;
	}


	public void set(T value) {
		this.value = value;
	}

}
