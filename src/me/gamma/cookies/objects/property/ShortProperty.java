
package me.gamma.cookies.objects.property;


public class ShortProperty extends PrimitiveProperty<Short> {

	public ShortProperty(String name) {
		super(name);
	}


	@Override
	public Class<Short> getPrimitiveClass() {
		return Short.class;
	}


	@Override
	public Short emptyValue() {
		return 0;
	}

}
