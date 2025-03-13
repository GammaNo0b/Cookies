
package me.gamma.cookies.util;


import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



public class DebugUtils {

	/**
	 * Prints the current Stack Trace to the standard output stream.
	 */
	public static void printStackTrace() {
		Thread thread = Thread.currentThread();
		new Exception(String.format("Stacktrace of Thread %s [%d]", thread.getName(), thread.threadId())).printStackTrace(System.out);
	}


	/**
	 * Prints the given object to the current output stream and returns always true. This method can be used inside boolean calculations in combination
	 * with the bit-wise and operation, so that the result won't be affected but the method still executed.
	 * 
	 * @param o the object
	 * @return true
	 */
	public static boolean println(Object x) {
		System.out.println(x);
		return true;
	}


	/**
	 * Creates a String representation of this object using the java reflection framework.
	 * 
	 * @param object the object to be converted to a string.
	 * @return the string
	 */
	public static String toString(Object object) {
		return toString(object, 0, 0);
	}


	/**
	 * Creates a String representation of this object using the java reflection framework.
	 * 
	 * @param object the object to be converted to a string.
	 * @param depth  how deep the fields in the object should be also converted to string
	 * @return the string
	 */
	public static String toString(Object object, int level, int levels) {
		final Set<Class<?>> canBeString = new HashSet<>(Arrays.asList(Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, String.class));
		StringBuilder builder = new StringBuilder();
		final String fill = ArrayUtils.fill(' ', (levels - level) * 2);
		builder.append(fill + "{\n");
		for(Field field : object.getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				builder.append(fill + "  " + field.getName() + "=\"");
				Object value = field.get(object);
				if(level <= 0 || value == null || canBeString.contains(value.getClass())) {
					builder.append(value);
				} else {
					builder.append(toString(value, level - 1, levels));
				}
				builder.append("\"\n");
			} catch(IllegalArgumentException | IllegalAccessException | InaccessibleObjectException e) {
				e.printStackTrace();
			}
		}
		builder.append(fill + "}");
		return builder.toString();
	}

}
