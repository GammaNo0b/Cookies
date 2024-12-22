
package me.gamma.cookies.util.collection;


import java.util.Iterator;



public class RangeIterator implements Iterator<Integer> {

	private final int end;
	private int index;

	/**
	 * Iterates from 0 to the given end value in increasing steps.
	 * 
	 * @param end the end value
	 */
	public RangeIterator(int end) {
		this.end = end;
		this.index = 0;
	}


	/**
	 * Creates an iterator that iterates from the start (inclusive) to the end (exclusive).
	 * 
	 * @param start the starting index
	 * @param end   the ending index
	 */
	public RangeIterator(int start, int end) {
		this.end = end;
		this.index = start;
	}


	@Override
	public boolean hasNext() {
		return index < end;
	}


	@Override
	public Integer next() {
		return this.index++;
	}

}
