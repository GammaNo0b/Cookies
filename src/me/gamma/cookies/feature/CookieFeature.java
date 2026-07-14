
package me.gamma.cookies.feature;


public interface CookieFeature {

	/**
	 * Is called once when the plugin is (re-) starting.
	 */
	void register();

	void setEnabled(boolean enabled);

	boolean isEnabled();

}
