
package me.gamma.cookies.util.math;


public class GenericIDRegister extends IDRegister<Object> {

	@SuppressWarnings("unchecked")
	public <T> T unregisterGeneric(int id) {
		try {
			return (T) super.unregister(id);
		} catch(ClassCastException _) {
			return null;
		}
	}


	@SuppressWarnings("unchecked")
	public <T> T getGeneric(int id) {
		try {
			return (T) super.getValue(id);
		} catch(ClassCastException _) {
			return null;
		}
	}

}
