
package me.gamma.cookies.util.collection;


import java.util.Iterator;



public class ArrayIterator<E> implements Iterator<E> {

	private final E[] array;
	private int index = 0;

	public ArrayIterator(E[] array) {
		this.array = array;
	}


	@Override
	public boolean hasNext() {
		return this.index < this.array.length;
	}


	@Override
	public E next() {
		return this.array[this.index++];
	}

}
