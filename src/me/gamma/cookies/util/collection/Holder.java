
package me.gamma.cookies.util.collection;


import java.util.function.Consumer;

import com.google.common.base.Supplier;



public interface Holder<T> {

	T get();

	void set(T value);


	public static <T> Holder<T> create(final Supplier<T> getter, final Consumer<T> setter) {
		return new Holder<T>() {

			@Override
			public T get() {
				return getter.get();
			}


			@Override
			public void set(T value) {
				setter.accept(value);
			}

		};
	}

	public static class BasicHolder<T> implements Holder<T> {

		public T value;

		public BasicHolder() {
			this.value = null;
		}


		public BasicHolder(T value) {
			this.value = value;
		}


		public T get() {
			return this.value;
		}


		public void set(T value) {
			this.value = value;
		}

	}

}
