
package me.gamma.cookies.objects.block;


import java.util.List;

import org.bukkit.block.TileState;

import me.gamma.cookies.objects.Fluid;



public interface FluidConsumer {

	List<FluidProvider> getInputFluidHolders(TileState block);


	default Fluid addFluid(TileState block, Fluid fluid) {
		int millibuckets = fluid.getMillibuckets();
		for(FluidProvider provider : this.getInputFluidHolders(block)) {
			if(millibuckets <= 0)
				break;
			if(provider.getType() != fluid.getType())
				continue;
			millibuckets = provider.set(millibuckets);
		}
		fluid.setMilliBuckets(millibuckets);
		return fluid;
	}

}
