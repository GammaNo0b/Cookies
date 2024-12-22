
package me.gamma.cookies.object.property;


public class EnergyProperty extends IntegerProperty {

	public EnergyProperty(String name) {
		super(name);
	}


	public EnergyProperty(String name, int max) {
		super(name, Integer.MIN_VALUE, max);
	}


	public EnergyProperty(String name, int min, int max) {
		super(name, min, max);
	}

}
