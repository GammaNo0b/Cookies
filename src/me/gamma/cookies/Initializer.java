
package me.gamma.cookies;


public class Initializer {

	private final String name;
	private final Runnable init;

	public Initializer(String name, Runnable init) {
		this.name = name;
		this.init = init;
	}


	public void init() {
		System.out.printf("[%s] Initialize %s ...", Cookies.INSTANCE.getName(), this.name);
		long time = System.currentTimeMillis();
		this.init.run();
		time = System.currentTimeMillis() - time;
		System.out.printf(" Done! [%dms]\n", time);
	}

}
