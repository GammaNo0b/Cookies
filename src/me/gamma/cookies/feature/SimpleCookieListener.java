
package me.gamma.cookies.feature;


public class SimpleCookieListener implements CookieListener {

	private final String name;
	protected boolean enabled = false;

	public SimpleCookieListener(String name) {
		this.name = name;
	}


	@Override
	public String getName() {
		return this.name;
	}


	public boolean isEnabled() {
		return this.enabled;
	}


	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
