
package me.gamma.cookies.util;


import java.util.Objects;
import java.util.function.Function;



public class EnumUtils {

	public static <E extends Enum<E>> E byIndex(Class<E> clazz, int index) {
		E[] constants = clazz.getEnumConstants();
		return constants[Math.floorMod(index, constants.length)];
	}


	public static <E extends Enum<E>, T> E byMember(Class<E> clazz, Function<E, T> getter, T member) {
		E[] constants = clazz.getEnumConstants();
		for(E element : constants)
			if(Objects.equals(getter.apply(element), member))
				return element;

		return null;
	}


	public static <E extends Enum<E>> E cycle(E element) {
		return cycle(element, 1);
	}


	public static <E extends Enum<E>> E cycle(E element, int amount) {
		return byIndex(element.getDeclaringClass(), element.ordinal() + amount);
	}

}
