
package me.gamma.cookies.util;


public class MathHelper {

	public static int intpow(int b, int e) {
		if(e == 0) {
			return 1;
		} else {
			int a = intpow(b, e / 2);
			a *= a;
			if(e % 2 == 1)
				a *= b;
			return a;
		}
	}


	public static int max(int... is) {
		if(is.length == 0) {
			return 0;
		} else if(is.length == 1) {
			return is[0];
		} else {
			int max = is[0];
			for(int i = 1; i < is.length; i++)
				if(max < is[i])
					max = is[i];
			return max;
		}
	}
	
	public static int min(int... is) {
		if(is.length == 0) {
			return 0;
		} else if(is.length == 1) {
			return is[0];
		} else {
			int min = is[0];
			for(int i = 1; i < is.length; i++)
				if(min > is[i])
					min = is[i];
			return min;
		}
	}


	public static double map(double x, double minfrom, double maxfrom, int minto, int maxto) {
		return (x - minfrom) / (maxfrom - minfrom) * (maxto + minto) + minto;
	}

}
