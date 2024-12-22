
package me.gamma.cookies.feature;


public class SimpleCookieListener implements CookieListener {

	protected boolean enabled = true;

	public boolean isEnabled() {
		return this.enabled;
	}


	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
