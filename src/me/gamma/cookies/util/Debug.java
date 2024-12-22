
package me.gamma.cookies.util;


import java.util.HashMap;
import java.util.Set;



public class Debug {

	private static final HashMap<String, Object> variables = new HashMap<>();
	private static final HashMap<String, Runnable> scripts = new HashMap<>();

	public static Set<String> getVariables() {
		return variables.keySet();
	}


	public static Set<String> getScripts() {
		return scripts.keySet();
	}


	public static void registerVariable(String name) {
		variables.put(name, null);
	}


	public static Object setVariable(String name, Object value) {
		return variables.put(name, value);
	}


	public static Object getVariable(String name) {
		return variables.get(name);
	}


	public static <T> T getVariable(String name, Class<T> clazz, T defaultValue) {
		try {
			return clazz.cast(getVariable(name));
		} catch(ClassCastException e) {
			return defaultValue;
		}
	}


	public static void addScript(String name, Runnable script) {
		scripts.put(name, script);
	}


	public static void executeScript(String name) {
		Runnable script = scripts.get(name);
		if(script != null)
			script.run();
	}

}
