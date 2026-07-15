
package me.gamma.cookies.feature;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;



public class CookieFeatures {

	private final Map<String, CookieFeature> features = new HashMap<>();

	public void reset() {
		this.features.clear();
	}


	public boolean isEmpty() {
		return this.features.isEmpty();
	}


	public int size() {
		return this.features.size();
	}


	public void register(CookieFeature feature) {
		this.features.put(feature.getName(), feature);
		feature.register();
	}


	public CookieFeature getFeature(String name) {
		return this.features.get(name);
	}


	public Stream<CookieFeature> stream() {
		return this.features.values().stream();
	}

}
