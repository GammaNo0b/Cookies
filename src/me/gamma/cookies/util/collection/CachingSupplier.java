
package me.gamma.cookies.util.collection;


import java.util.function.Supplier;



public class CachingSupplier<T> implements Supplier<T> {

	private final Supplier<T> supplier;
	private T value;

	public CachingSupplier(Supplier<T> supplier) {
		this.supplier = supplier;
	}


	@Override
	public T get() {
		if(this.value == null)
			this.update();
		return this.value;
	}


	public void update() {
		this.value = this.supplier.get();
	}
	
	public void clear() {
		this.value = null;
	}

}
