
package me.gamma.cookies.util;


public interface Provider<T> {

	T get();

	void set(T value);

}
