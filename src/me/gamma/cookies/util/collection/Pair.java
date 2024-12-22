
package me.gamma.cookies.util.collection;


import java.util.Objects;



public class Pair<L, R> {

	public L left;
	public R right;

	public Pair() {
		this(null, null);
	}


	public Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}


	@Override
	public String toString() {
		return Objects.toString(this.left) + ": " + Objects.toString(this.right);
	}

}
