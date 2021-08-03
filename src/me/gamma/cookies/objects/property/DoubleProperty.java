
package me.gamma.cookies.objects.property;


public class DoubleProperty extends PrimitiveProperty<Double> {

	public DoubleProperty(String name) {
		super(name);
	}


	@Override
	public Class<Double> getPrimitiveClass() {
		return Double.class;
	}


	@Override
	public Double emptyValue() {
		return 0.0D;
	}

}
