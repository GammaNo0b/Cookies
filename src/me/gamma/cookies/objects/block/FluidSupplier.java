
package me.gamma.cookies.objects.block;


import java.util.List;

import org.bukkit.block.TileState;

import me.gamma.cookies.objects.Fluid;
import me.gamma.cookies.objects.FluidType;



public interface FluidSupplier {

	List<FluidProvider> getOutputFluidHolders(TileState block);


	default Fluid removeFluid(TileState block, int max) {
		Fluid fluid = new Fluid(null);
		for(FluidProvider provider : this.getOutputFluidHolders(block)) {
			if(fluid.getType() != null && fluid.getType() != provider.getType())
				continue;
			fluid.setType(provider.getType());
			int millibuckets = provider.get(max);
			max -= millibuckets;
			fluid.grow(millibuckets);
			if(max <= 0)
				break;
		}
		return fluid;
	}


	default Fluid removeFluid(TileState block, FluidType type, int max) {
		Fluid fluid = new Fluid(type);
		for(FluidProvider provider : this.getOutputFluidHolders(block)) {
			if(type != provider.getType())
				continue;
			int millibuckets = provider.get(max);
			max -= millibuckets;
			fluid.grow(millibuckets);
			if(max <= 0)
				break;
		}
		return fluid;
	}

}
