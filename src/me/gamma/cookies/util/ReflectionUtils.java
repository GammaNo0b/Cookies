
package me.gamma.cookies.util;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



public class ReflectionUtils {

	public static Field findField(Class<?> owner, Class<?> type, int position) {
		for(Field field : owner.getDeclaredFields())
			if(field.getType().equals(type))
				if(--position == 0)
					return field;
		return null;
	}


	@SuppressWarnings("unchecked")
	public static <T> T getValueFromField(Field field, Object instance) {
		try {
			field.setAccessible(true);
			return (T) field.get(instance);
		} catch(IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static void setValueFromField(Field field, Object instance, Object value) {
		try {
			field.setAccessible(true);
			field.set(instance, value);
		} catch(IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}


	public static <T> T findAndGetValueFromField(Class<?> owner, Object instance, Class<T> type, int position) {
		Field field = findField(owner, type, position);
		return getValueFromField(field, instance);
	}


	public static <T> T findAndGetValueFromField(Object instance, Class<T> type, int position) {
		return findAndGetValueFromField(instance.getClass(), instance, type, position);
	}


	public static void findAndSetValueFromField(Class<?> owner, Object instance, Class<?> type, Object value, int position) {
		Field field = findField(owner, type, position);
		setValueFromField(field, instance, value);
	}


	public static void findAndSetValueFromField(Object instance, Object value, int position) {
		findAndSetValueFromField(instance.getClass(), instance, value.getClass(), value, position);
	}


	public static Method findMethod(Class<?> owner, Class<?> returnType, Class<?>... parameterTypes) {
		for(Method method : owner.getDeclaredMethods()) {
			if(method.getReturnType().equals(returnType)) {
				Class<?>[] types = method.getParameterTypes();
				if(types.length != parameterTypes.length)
					continue;
				boolean inequal = false;
				for(int i = 0; i < types.length; i++) {
					if(!types[i].equals(parameterTypes[i])) {
						inequal = true;
						break;
					}
				}
				if(!inequal)
					return method;
			}
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	public static <T> T invokeMethod(Method method, Object instance, Object... parameters) {
		try {
			method.setAccessible(true);
			return (T) method.invoke(instance, parameters);
		} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassCastException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static <T> T findAndInvokeMethod(Class<?> owner, Object instance, Class<T> returnType, Object... parameters) {
		Class<?>[] parameterTypes = new Class<?>[parameters.length];
		for(int i = 0; i < parameters.length; i++)
			parameterTypes[i] = parameters[i].getClass();
		Method method = findMethod(owner, returnType, parameterTypes);
		return invokeMethod(method, instance, parameters);
	}


	public static <T> T findAndInvokeMethod(Object instance, Class<T> returnType, Object... parameters) {
		return findAndInvokeMethod(instance.getClass(), instance, returnType, parameters);
	}

}
