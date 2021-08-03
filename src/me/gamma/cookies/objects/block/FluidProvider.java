
package me.gamma.cookies.objects.block;


import org.bukkit.block.TileState;

import me.gamma.cookies.objects.Fluid;
import me.gamma.cookies.objects.FluidType;
import me.gamma.cookies.objects.property.FluidProperty;
import me.gamma.cookies.objects.property.IntegerProperty;



public interface FluidProvider {

	FluidType getType();

	int get(int max);

	int set(int millibuckets);


	static FluidProvider fromFluid(final Fluid fluid) {
		return new FluidProvider() {

			@Override
			public FluidType getType() {
				return fluid.getType();
			}


			@Override
			public int get(int max) {
				int amount = fluid.getMillibuckets();
				int millibuckets = Math.min(max, amount);
				fluid.shrink(millibuckets);
				return millibuckets;
			}


			@Override
			public int set(int millibuckets) {
				fluid.grow(millibuckets);
				return 0;
			}

		};
	}


	static FluidProvider fromProperty(final FluidType type, final IntegerProperty property, final TileState block) {
		return new FluidProvider() {

			@Override
			public FluidType getType() {
				return type;
			}


			@Override
			public int get(int max) {
				int amount = property.fetch(block);
				int millibuckets = Math.min(max, amount);
				property.store(block, amount - millibuckets);
				block.update();
				return millibuckets;
			}


			@Override
			public int set(int millibuckets) {
				property.increase(block, millibuckets);
				block.update();
				return 0;
			}

		};
	}


	static FluidProvider fromProperty(final FluidProperty property, final TileState block) {
		return new FluidProvider() {

			@Override
			public FluidType getType() {
				return property.getType();
			}


			@Override
			public int get(int max) {
				Fluid fluid = property.fetch(block);
				int millibuckets = Math.min(max, fluid.getMillibuckets());
				fluid.shrink(millibuckets);
				property.store(block, fluid);
				block.update();
				return millibuckets;
			}


			@Override
			public int set(int millibuckets) {
				Fluid fluid = property.fetch(block);
				fluid.grow(millibuckets);
				property.store(block, fluid);
				block.update();
				return 0;
			}

		};
	}

}
