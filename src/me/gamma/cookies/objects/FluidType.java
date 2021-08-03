
package me.gamma.cookies.objects;


import me.gamma.cookies.objects.property.FluidProperty;



public enum FluidType {

	WATER, LAVA;

	public FluidProperty createProperty() {
		return new FluidProperty(this.name().toLowerCase(), this);
	}


	public static FluidType fromInt(int i) {
		FluidType[] values = values();
		return i >= 0 && i < values.length ? values[i] : null;
	}

}
