
package me.gamma.cookies.objects.property;


public class ByteProperty extends PrimitiveProperty<Byte> {

	public ByteProperty(String name) {
		super(name);
	}


	@Override
	public Class<Byte> getPrimitiveClass() {
		return Byte.class;
	}


	@Override
	public Byte emptyValue() {
		return 0;
	}

}
