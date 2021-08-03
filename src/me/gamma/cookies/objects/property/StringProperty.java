
package me.gamma.cookies.objects.property;


public class StringProperty extends PrimitiveProperty<String> {

	public StringProperty(String name) {
		super(name);
	}


	@Override
	public Class<String> getPrimitiveClass() {
		return String.class;
	}


	@Override
	public String emptyValue() {
		return "";
	}

}
