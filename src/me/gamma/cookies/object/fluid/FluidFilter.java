
package me.gamma.cookies.object.fluid;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.gamma.cookies.object.Filter;



public class FluidFilter implements Filter<FluidType> {

	private final List<FluidType> types;
	private final boolean whitelisted;

	public FluidFilter(boolean whitelist) {
		this(new ArrayList<>(), whitelist);
	}


	public FluidFilter(List<FluidType> types, boolean whitelisted) {
		this.whitelisted = whitelisted;
		this.types = types;
	}


	public void addFilterItem(FluidType type) {
		this.types.add(type);
	}


	public boolean isWhitelisted() {
		return this.whitelisted;
	}


	@Override
	public int filter(FluidType type, int amount) {
		return this.types.contains(type) == this.whitelisted ? amount : 0;
	}


	@Override
	public String toString() {
		return String.format("Whitelisted: %b, IgnoreNBT: %b, Types: %s", this.whitelisted, Arrays.toString(this.types.toArray(new FluidType[this.types.size()])));
	}

}
