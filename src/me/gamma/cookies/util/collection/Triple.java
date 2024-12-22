
package me.gamma.cookies.util.collection;


public class Triple<L, M, R> {

	public L left;
	public M middle;
	public R right;

	public Triple() {
		this(null, null, null);
	}


	public Triple(L left, M middle, R right) {
		this.left = left;
		this.middle = middle;
		this.right = right;
	}

}
