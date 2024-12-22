
package me.gamma.cookies.util;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;



public class ReflectionUtils {

	public static Field findField(Class<?> clazz, int index) {
		for(Field field : getAllDeclaredFields(clazz))
			if(--index == 0)
				return field;
		return null;
	}


	public static Field findField(Class<?> clazz, Class<?> type, int index) {
		for(Field field : getAllDeclaredFields(clazz))
			if(field.getType() == type)
				if(--index == 0)
					return field;
		return null;
	}


	@SuppressWarnings("unchecked")
	public static <T> T getValue(Field field, Object instance) {
		try {
			field.setAccessible(true);
			return (T) field.get(instance);
		} catch(IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static void setValue(Field field, Object instance, Object value) {
		try {
			field.setAccessible(true);
			field.set(instance, value);
		} catch(IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}


	public static Method findMethod(Class<?> clazz, int index) {
		for(Method method : getAllDeclaredMethods(clazz))
			if(--index == 0)
				return method;
		return null;
	}


	public static Method findMethod(Class<?> clazz, Class<?> returnType, String name) {
		for(Method method : getAllDeclaredMethods(clazz))
			if(method.getName().equals(name) && method.getReturnType() == returnType)
				return method;
		return null;
	}


	public static Method findMethod(Class<?> clazz, Class<?> returnType, int index) {
		for(Method method : getAllDeclaredMethods(clazz))
			if(method.getReturnType() == returnType)
				if(--index == 0)
					return method;
		return null;
	}


	public static Method findMethod(Class<?> clazz, Class<?> returnType, int index, Class<?>[] parameterTypes) {
		for(Method method : getAllDeclaredMethods(clazz))
			if(method.getReturnType() == returnType)
				if(Arrays.equals(parameterTypes, method.getParameterTypes()))
					if(--index == 0)
						return method;
		return null;
	}


	@SuppressWarnings("unchecked")
	public static <T> T invokeMethod(Method method, Object instance, Object... parameters) {
		try {
			method.setAccessible(true);
			return (T) method.invoke(instance, parameters);
		} catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static List<Field> getAllDeclaredFields(Class<?> clazz) {
		List<Field> fields = new ArrayList<>();
		while(clazz != null) {
			for(Field field : clazz.getDeclaredFields())
				fields.add(field);
			clazz = clazz.getSuperclass();
		}
		return fields;
	}


	public static List<Method> getAllDeclaredMethods(Class<?> clazz) {
		List<Method> methods = new ArrayList<>();
		while(clazz != null) {
			for(Method method : clazz.getDeclaredMethods())
				methods.add(method);
			clazz = clazz.getSuperclass();
		}
		return methods;
	}


	@SuppressWarnings("unchecked")
	public static <T> ClassWrapper<T> wrapClass(String name) {
		try {
			return (ClassWrapper<T>) ClassWrapper.wrap(Class.forName(name));
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			return ClassWrapper.wrap(null);
		}
	}


	public static <T> ClassWrapper<T> wrapClass(Class<T> clazz) {
		return ClassWrapper.wrap(clazz);
	}


	public static <T> ClassWrapper<T> wrapClass(Class<T> clazz, T instance) {
		return ClassWrapper.wrap(clazz, instance);
	}

	public static class ClassWrapper<T> {

		private final Class<T> clazz;
		private T instance;

		private ClassWrapper(Class<T> clazz) {
			this.clazz = clazz;
		}


		public Class<T> getWrappedClass() {
			return this.clazz;
		}


		public ClassWrapper<T> setInstance(T instance) {
			this.instance = instance;
			return this;
		}


		public <F> FieldWrapper<T, F> wrapField(Field field) {
			return FieldWrapper.wrap(field, this.instance);
		}


		public <F> FieldWrapper<T, F> wrapField(int index) {
			return this.wrapField(findField(this.clazz, index));
		}


		public <F> FieldWrapper<T, F> wrapField(Class<F> type, int index) {
			return this.wrapField(findField(this.clazz, type, index));
		}


		public <F> MethodWrapper<T, F> wrapMethod(Method method) {
			return MethodWrapper.wrap(method, this.instance);
		}


		public <F> MethodWrapper<T, F> wrapMethod(String name, Class<F> returnType) {
			return MethodWrapper.wrap(findMethod(this.clazz, returnType, name));
		}


		public <F> MethodWrapper<T, F> wrapMethod(int index) {
			return this.wrapMethod(findMethod(this.clazz, index));
		}


		public <F> MethodWrapper<T, F> wrapMethod(Class<F> returnType, int index) {
			return this.wrapMethod(findMethod(this.clazz, returnType, index));
		}


		public <F> MethodWrapper<T, F> wrapMethod(Class<F> returnType, int index, Class<?>[] parameterTypes) {
			return this.wrapMethod(findMethod(this.clazz, returnType, index, parameterTypes));
		}


		public static <T> ClassWrapper<T> wrap(Class<T> clazz) {
			return new ClassWrapper<T>(clazz);
		}


		public static <T> ClassWrapper<T> wrap(Class<T> clazz, T instance) {
			return new ClassWrapper<T>(clazz).setInstance(instance);
		}

	}

	public static class FieldWrapper<T, F> {

		private final Field field;
		private T instance;
		private F value;

		private FieldWrapper(Field field) {
			this.field = field;
		}


		public FieldWrapper<T, F> setInstance(T instance) {
			this.instance = instance;
			return this;
		}


		@SuppressWarnings("unchecked")
		public FieldWrapper<T, F> fetch() {
			this.value = (F) ReflectionUtils.getValue(this.field, this.instance);
			return this;
		}


		public FieldWrapper<T, F> store() {
			ReflectionUtils.setValue(this.field, this.instance, this.value);
			return this;
		}


		public FieldWrapper<T, F> setValue(F value) {
			this.value = value;
			return this;
		}


		public FieldWrapper<T, F> getValue(Consumer<F> consumer) {
			consumer.accept(this.value);
			return this;
		}


		public F getValue() {
			return this.value;
		}


		@SuppressWarnings("unchecked")
		public <G> FieldWrapper<T, G> cast(Class<G> clazz) {
			FieldWrapper<T, G> wrapper = wrap(this.field, this.instance);
			wrapper.setValue((G) this.value);
			return wrapper;
		}


		public static <T, F> FieldWrapper<T, F> wrap(Field field) {
			return new FieldWrapper<>(field);
		}


		public static <T, F> FieldWrapper<T, F> wrap(Field field, T instance) {
			return new FieldWrapper<T, F>(field).setInstance(instance);
		}

	}

	public static class MethodWrapper<T, F> {

		private final Method method;
		private T instance;
		private Object[] parameters;
		private F value;

		public MethodWrapper(Method method) {
			this.method = method;
		}


		public MethodWrapper<T, F> setInstance(T instance) {
			this.instance = instance;
			return this;
		}


		public MethodWrapper<T, F> setParameters(Object... parameters) {
			this.parameters = parameters;
			return this;
		}


		public MethodWrapper<T, F> invoke() {
			this.value = invokeMethod(this.method, this.instance, this.parameters);
			return this;
		}


		public MethodWrapper<T, F> getValue(Consumer<F> consumer) {
			consumer.accept(this.value);
			return this;
		}


		public F getValue() {
			return this.value;
		}


		@SuppressWarnings("unchecked")
		public <G> MethodWrapper<T, G> cast(Class<G> clazz) {
			MethodWrapper<T, G> wrapper = wrap(this.method, this.instance);
			wrapper.setParameters(this.parameters);
			wrapper.value = (G) this.value;
			return wrapper;
		}


		public static <T, F> MethodWrapper<T, F> wrap(Method method) {
			return new MethodWrapper<>(method);
		}


		public static <T, F> MethodWrapper<T, F> wrap(Method method, T instance) {
			return new MethodWrapper<T, F>(method).setInstance(instance);
		}

	}

}
