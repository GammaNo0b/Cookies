
package me.gamma.cookies.objects.property;


public class ByteArrayProperty extends PrimitiveProperty<byte[]> {

	public ByteArrayProperty(String name) {
		super(name);
	}


	@Override
	public Class<byte[]> getPrimitiveClass() {
		return byte[].class;
	}


	@Override
	public byte[] emptyValue() {
		return new byte[0];
	}

}
