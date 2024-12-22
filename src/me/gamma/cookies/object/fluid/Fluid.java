
package me.gamma.cookies.object.fluid;


public class Fluid {

	private FluidType type;
	private int millibuckets;

	public Fluid(FluidType type) {
		this.type = type;
	}


	public Fluid(FluidType type, int millibuckets) {
		this.type = type;
		this.millibuckets = millibuckets;
	}


	public FluidType getType() {
		return this.type;
	}


	public void setType(FluidType type) {
		this.type = type;
	}


	public boolean isEmpty() {
		return this.type == null || this.type == FluidType.EMPTY || this.millibuckets == 0;
	}


	public int getMillibuckets() {
		return this.millibuckets;
	}


	public void setMillibuckets(int millibuckets) {
		this.millibuckets = millibuckets;
	}


	public void addMillibuckets(int millibuckets) {
		this.millibuckets += millibuckets;
	}


	public void grow(int millibuckets) {
		this.millibuckets += millibuckets;
	}


	public void shrink(int millibuckets) {
		this.grow(-millibuckets);
	}


	@Override
	public String toString() {
		return String.format("%s: %d", this.type.name(), this.millibuckets);
	}


	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Fluid))
			return false;
		Fluid fluid = (Fluid) obj;
		return this.type == fluid.type && this.millibuckets == fluid.millibuckets;
	}


	@Override
	public int hashCode() {
		return (this.type.ordinal() << 8 | this.type.ordinal() << 4 | this.type.ordinal()) ^ this.millibuckets;
	}

}
