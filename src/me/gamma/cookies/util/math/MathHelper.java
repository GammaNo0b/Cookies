
package me.gamma.cookies.util.math;


import java.util.Random;
import java.util.function.Predicate;



public class MathHelper {

	/**
	 * Global {@link Random} instance for various purposes.
	 */
	public static final Random random = new Random();

	/**
	 * Returns the result of n % m.
	 * 
	 * @param n
	 * @param m
	 * @return n % m
	 */
	public static int mod(int n, int m) {
		return ((n % m) + m) % m;
	}


	/**
	 * Returns the smallest integer n greater or equal to a / b. Equivalent to ceil(a/b).
	 * 
	 * @param a the numerator
	 * @param b the denominator
	 * @return ceil of a / b
	 */
	public static int ceildiv(int a, int b) {
		return (a + b - 1) / b;
	}


	/**
	 * Calculates recursively the expression b<sup>e</sup>.
	 * 
	 * @param b the base as an integer
	 * @param e the exponent as an integer
	 * @return
	 */
	public static int intpow(int b, int e) {
		if(e == 0) {
			return 1;
		} else if(e > 0) {
			int a = intpow(b, e / 3);
			a *= a * a;
			int m = e % 3;
			if(m > 0) {
				a *= b;
				if(m > 1) {
					a *= b;
				}
			}
			return a;
		} else {
			return -1;
		}
	}


	/**
	 * Calculates recursively the expression b<sup>e</sup>.
	 * 
	 * @param b the base as a double
	 * @param e the exponent as an integer
	 * @return
	 */
	public static double intpow(double b, int e) {
		if(e == 0) {
			return 1;
		} else if(e < 0) {
			return 1.0D / intpow(b, -e);
		} else {
			double a = intpow(b, e / 3);
			a *= a * a;
			int m = e % 3;
			if(m > 0) {
				a *= b;
				if(m > 1) {
					a *= b;
				}
			}
			return a;
		}
	}


	/**
	 * Returns the maximum value from the given values.
	 * 
	 * @param values the values
	 * @return the maximum value
	 */
	public static int max(int... values) {
		if(values.length == 0) {
			return 0;
		} else if(values.length == 1) {
			return values[0];
		} else {
			int max = values[0];
			for(int i = 1; i < values.length; i++)
				if(max < values[i])
					max = values[i];
			return max;
		}
	}


	/**
	 * Returns the minimum value from the given values.
	 * 
	 * @param values the values
	 * @return the minimum value
	 */
	public static int min(int... values) {
		if(values.length == 0) {
			return 0;
		} else if(values.length == 1) {
			return values[0];
		} else {
			int min = values[0];
			for(int i = 1; i < values.length; i++)
				if(min > values[i])
					min = values[i];
			return min;
		}
	}


	/**
	 * Maps the value from the interval [0;from] to [0;to].
	 * 
	 * @param x    value to be mapped
	 * @param from the original interval
	 * @param to   the new interval
	 * @return the mapped value
	 */
	public static double map(double x, double from, double to) {
		return x * to / from;
	}


	/**
	 * Maps the value from the interval [minfrom;maxfrom] to [minto;maxto].
	 * 
	 * @param x       value to be mapped
	 * @param minfrom start of the original interval
	 * @param maxfrom end of the original interval
	 * @param minto   strat of the new interval
	 * @param maxto   end of the new interval
	 * @return the mapped value
	 */
	public static double map(double x, double minfrom, double maxfrom, double minto, double maxto) {
		return map(x - minfrom, maxfrom - minfrom, maxto - minto) + minto;
	}


	/**
	 * Returns x if x is between min and max, otherwise the value x is closer to is returned.
	 * 
	 * @param min the lower bound
	 * @param max the upper bound
	 * @param x   the value
	 * @return the clamped value
	 */
	public static int clamp(int min, int max, int x) {
		return x >= max ? max : x <= min ? min : x;
	}


	/**
	 * Generates random ids as long as the exists predicate returns true.
	 * 
	 * @param exists predicate that determines whether an id already exists
	 * @return the unique id
	 */
	public static int generateUniqueId(Predicate<Integer> exists) {
		int id;
		do {
			id = random.nextInt();
		} while(exists.test(id));
		return id;
	}

}
