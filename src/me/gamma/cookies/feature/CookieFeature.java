
package me.gamma.cookies.feature;


public interface CookieFeature {

	void register();
	
	void setEnabled(boolean enabled);
	
	boolean isEnabled();

}
